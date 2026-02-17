plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pricewise.feature.search.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
