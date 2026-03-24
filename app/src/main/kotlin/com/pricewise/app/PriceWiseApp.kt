package com.pricewise.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class PriceWiseApp : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        val imageOkHttpClient = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor { chain ->
                val imageRequest = chain.request().newBuilder()
                    .header("User-Agent", IMAGE_USER_AGENT)
                    .header("Accept", IMAGE_ACCEPT_HEADER)
                    .build()
                chain.proceed(imageRequest)
            }
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(imageOkHttpClient)
            .components {
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
    }

    private companion object {
        const val IMAGE_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14; PriceWise) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36"
        const val IMAGE_ACCEPT_HEADER =
            "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8"
    }
}
