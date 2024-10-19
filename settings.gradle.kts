pluginManagement {
    repositories {
        mavenCentral()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://mvnrepository.com/artifact") }
        maven { url = uri("https://mvnrepository.com") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://mvnrepository.com/artifact") }
        maven { url = uri("https://mvnrepository.com") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ComposedWeather"
include(":app")
include(":core-network")
include(":core-local")
include(":connectivity-android")
include(":data")
include(":domain")
include(":shared")
