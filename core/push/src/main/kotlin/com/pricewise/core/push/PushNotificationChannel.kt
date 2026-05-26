package com.pricewise.core.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object PushNotificationChannel {

    const val ID: String = "pricewise_price_drops"

    fun ensure(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(ID) != null) return
        val channel = NotificationChannel(
            ID,
            "Скидки и цены",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Уведомления о снижении цены на товары из избранного"
        }
        manager.createNotificationChannel(channel)
    }
}
