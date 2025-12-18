plugins {
    id("convention.kmp-compose-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.navigation.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.module.common.utils.common)

            implementation(projects.module.common.viewmodel)
            implementation(projects.module.uikit.designsystem)
            implementation(projects.module.uikit.appelements)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.core.navigation.api)

            implementation(projects.module.features.view.presentationApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.view.presentation.impl"
}