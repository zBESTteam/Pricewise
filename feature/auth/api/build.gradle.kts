plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.auth.api"
}

dependencies {
    implementation(project(":core:navigation-api"))
    implementation(libs.kotlinx.coroutines.core)
}
