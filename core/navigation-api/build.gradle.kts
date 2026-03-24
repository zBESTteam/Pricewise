import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.navigation.api"
}

dependencies {
    implementation(libs.foundation)
    implementation(libs.compose.ui)
    implementation(libs.androidx.navigation.compose)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.moduleName.set("navigation")
}
