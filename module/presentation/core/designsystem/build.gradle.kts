plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(libs.androidx.navigation.compose)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(compose.materialIconsExtended)

            implementation(projects.module.presentation.core.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.designsystem"
    buildFeatures {
        compose = true
    }
}
