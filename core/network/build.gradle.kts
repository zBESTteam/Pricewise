plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.core.network"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
