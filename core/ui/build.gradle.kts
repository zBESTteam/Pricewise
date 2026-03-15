plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pricewise.core.ui"

    buildFeatures {
        compose = true
        resValues = true
        viewBinding = true
    }
}

dependencies {
    api(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation("com.google.android.material:material:1.12.0")
}
