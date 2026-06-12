pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "OpenWeather"
include(":app")
include(":core:network")
include(":core:common")
include(":core:ui")
include(":feature:forecast:domain")
include(":feature:forecast:data")
include(":feature:forecast:presentation")
include(":feature:search:domain")
include(":feature:search:data")
include(":feature:search:presentation")
