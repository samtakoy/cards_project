plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.navigation.compose)

            implementation(projects.module.presentation.core.utils)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.designsystem"
    buildFeatures {
        compose = true
    }
}

dependencies {
    debugImplementation(compose.preview)
    debugImplementation(compose.components.uiToolingPreview)
 }