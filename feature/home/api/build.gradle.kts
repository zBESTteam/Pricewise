plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pricewise.feature.home.api"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:navigation-api"))
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.runtime)
}
