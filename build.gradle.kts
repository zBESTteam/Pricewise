import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

val compileSdkVersion = (project.findProperty("compileSdk") as String).toInt()
val minSdkVersion = (project.findProperty("minSdk") as String).toInt()
val targetSdkVersion = (project.findProperty("targetSdk") as String).toInt()
val javaVersion = JavaVersion.VERSION_17

subprojects {
    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension> {
            compileSdk = compileSdkVersion
            defaultConfig {
                minSdk = minSdkVersion
                targetSdk = targetSdkVersion
            }
            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }
        }
    }

    plugins.withId("com.android.library") {
        extensions.configure<LibraryExtension> {
            compileSdk = compileSdkVersion
            defaultConfig {
                minSdk = minSdkVersion
            }
            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }
        }
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure<KotlinAndroidProjectExtension> {
            jvmToolchain(21)
        }
    }

}
