plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.core.auth"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
