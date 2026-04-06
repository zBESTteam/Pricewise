plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pricewise.navigation.impl"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:navigation-api"))
    implementation(project(":core:ui"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:home:impl"))
    implementation(project(":feature:search:api"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:auth:api"))
    implementation(project(":feature:auth:impl"))

    implementation(platform(libs.compose.bom))
    implementation(libs.animation)
    implementation(libs.core.ktx)
    implementation(libs.foundation)
    implementation(libs.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
}
