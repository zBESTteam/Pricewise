plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.pricewise.feature.favorites.impl"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":feature:auth:api"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:favorites:api"))
    implementation(project(":core:ui"))
    implementation(project(":core:di"))
    implementation(project(":core:navigation-api"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.runtime)
    implementation(libs.material3)
    implementation(libs.compose.ui)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.runtime)

    val moshiVersion = "1.15.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)

    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
}
