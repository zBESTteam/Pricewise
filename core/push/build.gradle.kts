plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pricewise.core.push"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
