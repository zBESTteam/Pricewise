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
    implementation(platform(libs.compose.bom))
    implementation(libs.material)
    implementation(libs.foundation)
    implementation(libs.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.timber)
    api(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation("com.google.android.material:material:1.12.0")
}
