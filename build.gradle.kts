import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
}

val compileSdkVersion = (project.findProperty("compileSdk") as String).toInt()
val minSdkVersion = (project.findProperty("minSdk") as String).toInt()
val targetSdkVersion = (project.findProperty("targetSdk") as String).toInt()
val javaVersion = JavaVersion.VERSION_17
val detektVersion = libs.versions.detekt.get()

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

        configureDetekt(
            detektVersion = detektVersion,
            jvmTargetVersion = javaVersion.toString(),
        )
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

        configureDetekt(
            detektVersion = detektVersion,
            jvmTargetVersion = javaVersion.toString(),
        )
    }
}

tasks.register("detektAll") {
    group = "verification"
    description = "Runs detekt for all Android modules."
    dependsOn(
        subprojects
            .filter { subproject ->
                subproject.buildFile.exists()
            }
            .map { subproject ->
            "${subproject.path}:detekt"
            }
    )
}

tasks.register("detektBaselineAll") {
    group = "verification"
    description = "Generates detekt baselines for all Android modules."
    dependsOn(
        subprojects
            .filter { subproject ->
                subproject.buildFile.exists()
            }
            .map { subproject ->
                "${subproject.path}:detektBaseline"
            }
    )
}

fun Project.configureDetekt(
    detektVersion: String,
    jvmTargetVersion: String,
) {
    val baselineFileName = path.removePrefix(":").replace(':', '-') + ".xml"

    pluginManager.apply("dev.detekt")

    extensions.configure<DetektExtension> {
        toolVersion = detektVersion
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        baseline.set(rootProject.file("config/detekt/baselines/$baselineFileName"))
        buildUponDefaultConfig = true
        parallel = true
        ignoreFailures = false
        basePath.set(rootProject.projectDir)
    }

    dependencies.add(
        "detektPlugins",
        "dev.detekt:detekt-rules-ktlint-wrapper:$detektVersion",
    )

    tasks.withType(Detekt::class.java).configureEach {
        jvmTarget.set(jvmTargetVersion)
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/build/**")
        exclude("**/resources/**")

        reports {
            html.required.set(true)
            markdown.required.set(true)
            sarif.required.set(true)
            checkstyle.required.set(true)
        }
    }

    tasks.withType(DetektCreateBaselineTask::class.java).configureEach {
        jvmTarget.set(jvmTargetVersion)
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/build/**")
        exclude("**/resources/**")
    }

    tasks.named("check").configure {
        setDependsOn(
            dependsOn.filterNot { dependency ->
                dependency is TaskProvider<*> && dependency.name == "detekt"
            }
        )
    }
}
