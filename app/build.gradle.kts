plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pricewise.app"

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "com.pricewise.app"
        versionCode = (project.findProperty("versionCode") as String).toInt()
        versionName = project.findProperty("versionName") as String
    }
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:push"))

    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:home:impl"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:profile:impl"))
    implementation(project(":feature:favorites:impl"))
    implementation(project(":feature:product:impl"))
    implementation(project(":feature:photo_search:impl"))
    implementation(project(":feature:delivery:impl"))
    implementation(project(":feature:notifications:impl"))
    implementation(project(":feature:ads:impl"))

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
