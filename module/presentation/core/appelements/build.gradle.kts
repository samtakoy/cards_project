plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.navigation.compose)

            implementation(projects.module.presentation.core.utils)
            implementation(projects.module.presentation.core.designsystem)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.appelements"
}
