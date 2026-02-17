plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.auth.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
