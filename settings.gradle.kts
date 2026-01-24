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

include(":androidApp")
include(":desktopApp")
include(":iosUmbrella")

include(":module:common:utils:common")
include(":module:common:utils:presentation")
include(":module:common:viewmodel")

include(":module:core:log")
include(":module:core:resources")
include(":module:core:oldresources")
include(":module:core:navigation:api")
include(":module:core:network")
include(":module:core:uikit")
include(":module:core:cards:repo-api")
include(":module:core:cards:repo-impl")
include(":module:core:cards:model:card")
include(":module:core:cards:model:qpack")
include(":module:core:cards:model:theme")
include(":module:core:cards:model:learncourse")
include(":module:core:cards:model:view")

include(":module:features:card:api")
include(":module:features:card:impl")
include(":module:features:card:presentation-api")
include(":module:features:card:presentation-impl")
include(":module:features:qpack:api")
include(":module:features:qpack:impl")
include(":module:features:qpack:presentation-api")
include(":module:features:qpack:presentation-impl")
include(":module:features:theme:api")
include(":module:features:theme:impl")
include(":module:features:theme:presentation-api")
include(":module:features:theme:presentation-impl")
include(":module:features:learncourse:api")
include(":module:features:learncourse:impl")
include(":module:features:learncourse:presentation-api")
include(":module:features:learncourse:presentation-impl")
include(":module:features:view:api")
include(":module:features:view:impl")
include(":module:features:view:presentation-api")
include(":module:features:view:presentation-impl")
include(":module:features:favorites:api")
include(":module:features:favorites:impl")
include(":module:features:favorites:presentation-api")
include(":module:features:favorites:presentation-impl")
include(":module:features:settings:presentation-api")
include(":module:features:settings:presentation-impl")

include(":module:main:di")
include(":module:main:screen")

include(":module:services:task:api")
include(":module:services:task:impl")
include(":module:services:task:model")
include(":module:services:download:api")
include(":module:services:download:impl")
include(":module:services:notification:impl")
include(":module:services:permissions:api")
include(":module:services:permissions:impl")
include(":module:services:speech:api")
include(":module:services:speech:impl")
include(":module:services:speech:presentation")
include(":module:services:export:api")
include(":module:services:import:api")
include(":module:services:import:impl")