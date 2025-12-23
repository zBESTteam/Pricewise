package com.example.pricewise.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import java.io.File

private object PricewiseImageLoader {
    @Volatile
    private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        val appContext = context.applicationContext
        return instance ?: synchronized(this) {
            instance ?: build(appContext).also { instance = it }
        }
    }

    private fun build(context: Context): ImageLoader {
        val cacheDir = File(context.cacheDir, "image_cache")
        return ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .memoryCache { MemoryCache.Builder(context).maxSizePercent(0.25).build() }
            .diskCache { DiskCache.Builder().directory(cacheDir).maxSizeBytes(50L * 1024 * 1024).build() }
            .build()
    }
}

@Composable
fun rememberPricewiseImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context.applicationContext) {
        PricewiseImageLoader.get(context)
    }
}
