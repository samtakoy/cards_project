plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
            api(libs.kotlinx.serialization.json)

            api(projects.module.main.tabnavigation.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.main.tabnavigation.impl"
}