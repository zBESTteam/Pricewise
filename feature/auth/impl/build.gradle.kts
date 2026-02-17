plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.pricewise.feature.auth.impl"
}

dependencies {
    implementation(project(":feature:auth:api"))
    implementation(project(":core:di"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
}
