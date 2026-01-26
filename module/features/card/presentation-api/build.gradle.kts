plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)

            implementation(projects.module.core.navigation.api)
            implementation(projects.module.main.tabnavigation.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.card.presentation.api"
}
