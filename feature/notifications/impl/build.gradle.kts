plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.pricewise.feature.notifications.impl"
}

dependencies {
    implementation(project(":feature:notifications:api"))
    implementation(project(":core:di"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:push"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
}
