enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "cards"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
includeBuild("build-logic")

include(":app")
include(":module:data:common:api")
include(":module:data:common:impl")
include(":module:domain:card:api")
include(":module:domain:card:impl")
include(":module:domain:card:model")
include(":module:domain:qpack:api")
include(":module:domain:qpack:model")
include(":module:domain:qpack:impl")
include(":module:domain:theme:api")
include(":module:domain:theme:model")
include(":module:domain:theme:impl")
include(":module:domain:learncourse:api")
include(":module:domain:learncourse:model")
include(":module:domain:learncourse:impl")
include(":module:domain:view:api")
include(":module:domain:view:model")
include(":module:domain:view:impl")
include(":module:common:utils")
include(":module:platform:api")
include(":module:platform:impl")
include(":module:domain:favorites:api")
include(":module:domain:favorites:impl")
include(":module:presentation:core:designsystem")
include(":module:presentation:core:utils")
include(":module:presentation:core:viewmodel")
