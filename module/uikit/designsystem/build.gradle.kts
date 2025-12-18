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

            implementation(projects.module.common.utils.presentation)
        }
    }
}

android {
    namespace = "ru.samtakoy.uikit.designsystem"
    buildFeatures {
        compose = true
    }
}