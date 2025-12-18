plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)

            implementation(projects.module.core.navigation.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.settings.presentation.api"
}
