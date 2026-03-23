package com.pricewise.feature.home.impl.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pricewise.feature.home.impl.presentation.ui.placeholders.PlaceholderBox

@Composable
internal fun RemoteImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    shape: Shape?,
    contentScale: ContentScale,
    requestSize: DpSize?,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val requestWidthPx = remember(requestSize, density) {
        requestSize?.width?.let { width ->
            with(density) { width.roundToPx() }
        }
    }
    val requestHeightPx = remember(requestSize, density) {
        requestSize?.height?.let { height ->
            with(density) { height.roundToPx() }
        }
    }
    val imageRequest = remember(context, imageUrl, requestWidthPx, requestHeightPx) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .apply {
                if (requestWidthPx != null && requestHeightPx != null) {
                    size(requestWidthPx, requestHeightPx)
                }
            }
            .crossfade(false)
            .listener(
                onError = { request, result ->
                    Log.e(
                        HOME_IMAGE_LOG_TAG,
                        "Failed to load image: ${request.data}",
                        result.throwable,
                    )
                },
            )
            .build()
    }

    val imageModifier = if (shape == null) {
        Modifier.fillMaxSize()
    } else {
        Modifier
            .fillMaxSize()
            .clip(shape)
    }

    Box(modifier = modifier) {
        PlaceholderBox(
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            delayMillis = HomeLoadingDefaults.NoDelayMillis,
            useShimmer = false,
        )
        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = contentScale,
        )
    }
}

private const val HOME_IMAGE_LOG_TAG = "HomeRemoteImage"
