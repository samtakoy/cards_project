plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)

            // for @Immutable
            implementation(libs.androidx.compose.runtime)

            implementation(projects.module.presentation.navigation.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.themes.api"
}