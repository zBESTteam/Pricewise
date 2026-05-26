package com.pricewise.core.push

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pricewise.core.auth.TokenManager
import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.dto.NotificationAckRequestDto
import com.pricewise.core.network.dto.NotificationDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationPollWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val api: PriceWiseApi,
    private val tokenManager: TokenManager,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val token = tokenManager.getToken()?.trim().orEmpty()
        if (token.isEmpty()) return Result.success()

        val authHeader = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"

        val items = runCatching {
            api.getNotifications(
                authorization = authHeader,
                since = null,
                limit = 50,
            ).items.orEmpty()
        }.getOrElse {
            return Result.retry()
        }

        if (items.isEmpty()) return Result.success()

        PushNotificationChannel.ensure(applicationContext)
        val deliveredIds = mutableListOf<Long>()
        items.forEach { dto ->
            if (showNotification(dto)) {
                deliveredIds.add(dto.id)
            }
        }

        if (deliveredIds.isNotEmpty()) {
            runCatching {
                api.ackNotifications(
                    authorization = authHeader,
                    request = NotificationAckRequestDto(ids = deliveredIds),
                )
            }
        }
        return Result.success()
    }

    private fun showNotification(dto: NotificationDto): Boolean {
        val context = applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return false
        }

        val (title, text) = buildTitleAndText(dto) ?: return false

        val builder = NotificationCompat.Builder(context, PushNotificationChannel.ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)

        dto.productUrl?.takeIf { it.startsWith("http", ignoreCase = true) }?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pending = PendingIntent.getActivity(
                context,
                dto.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
            builder.setContentIntent(pending)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(dto.id.toInt(), builder.build())
        return true
    }

    private fun buildTitleAndText(dto: NotificationDto): Pair<String, String>? {
        val title = dto.title?.takeIf { it.isNotBlank() } ?: return null
        return when (dto.type) {
            "price_drop" -> {
                val old = dto.oldPrice
                val new = dto.newPrice
                if (old != null && new != null && old > new) {
                    val savings = old - new
                    "Скидка: $title" to "Цена упала с ${formatPrice(old)} до ${formatPrice(new)} (-${formatPrice(savings)})"
                } else {
                    "Скидка: $title" to "Цена обновилась"
                }
            }
            else -> "PriceWise" to title
        }
    }

    private fun formatPrice(value: Long): String =
        "%,d ₽".format(value).replace(',', ' ')
}
