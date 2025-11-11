plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.androidx.navigation.compose)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.navigation.api"
}