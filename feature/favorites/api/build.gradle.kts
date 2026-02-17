plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.favorites.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
