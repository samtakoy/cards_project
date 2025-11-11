plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)

            implementation(projects.module.presentation.navigation.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.settings.api"
}
