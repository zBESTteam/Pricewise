plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.core.push"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
