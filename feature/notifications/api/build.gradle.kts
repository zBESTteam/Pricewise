plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.notifications.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
