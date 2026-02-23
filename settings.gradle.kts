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
    ":core:navigation",
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
