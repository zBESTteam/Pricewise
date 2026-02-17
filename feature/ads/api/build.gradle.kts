plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.ads.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
