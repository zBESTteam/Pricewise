plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.pricewise.feature.search.impl"
}

dependencies {
    implementation(project(":feature:search:api"))
    implementation(project(":core:di"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation-api"))
    implementation(project(":core:network"))

    implementation(libs.core.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.foundation)
    implementation(libs.material3)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.compose.shimmer)
    implementation(libs.activity.compose)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
}
