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
include(":module:core:db:repo-api")
include(":module:core:db:repo-impl")
include(":module:core:db:model:card")
include(":module:core:db:model:qpack")
include(":module:core:db:model:theme")
include(":module:core:db:model:learncourse")
include(":module:core:db:model:view")

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
include(":module:features:export:api")
include(":module:features:import:api")
include(":module:features:import:impl")
include(":module:features:speech:api")
include(":module:features:speech:impl")
include(":module:features:speech:presentation")

include(":module:main:di")
include(":module:main:screen")

include(":module:uikit:appelements")
include(":module:uikit:designsystem")

include(":module:util-features:task:api")
include(":module:util-features:task:impl")
include(":module:util-features:task:model")
include(":module:util-features:download:api")
include(":module:util-features:download:impl")
include(":module:util-features:notification:impl")
include(":module:util-features:permissions:api")
include(":module:util-features:permissions:impl")




