plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            api(libs.kotlinx.serialization.json)
            implementation(libs.androidx.navigation.compose)

            // нужен только UiId
            implementation(projects.module.presentation.core.designsystem)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.navigation.api"
}