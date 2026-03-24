pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PriceWise"

include(
    ":app",
    ":core:di",
    ":core:navigation-api",
    ":core:navigation-impl",
    ":core:network",
    ":core:auth",
    ":core:push",
    ":core:ui",

    ":feature:auth:api",
    ":feature:auth:impl",
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:search:api",
    ":feature:search:impl",
    ":feature:profile:api",
    ":feature:profile:impl",
    ":feature:favorites:api",
    ":feature:favorites:impl",
    ":feature:product:api",
    ":feature:product:impl",
    ":feature:photo_search:api",
    ":feature:photo_search:impl",
    ":feature:delivery:api",
    ":feature:delivery:impl",
    ":feature:notifications:api",
    ":feature:notifications:impl",
    ":feature:ads:api",
    ":feature:ads:impl",
)

project(":core:navigation-api").projectDir = file("core/navigation-api")
project(":core:navigation-impl").projectDir = file("core/navigation-impl")
