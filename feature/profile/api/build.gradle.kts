plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.profile.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
