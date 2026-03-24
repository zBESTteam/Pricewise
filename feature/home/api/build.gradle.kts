plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.home.api"
}

dependencies {
    implementation(project(":core:navigation-api"))
    implementation(libs.kotlinx.coroutines.core)
}
