plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.pricewise.core.di"
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
