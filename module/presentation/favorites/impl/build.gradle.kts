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

            implementation(projects.module.common.utils)

            implementation(projects.module.presentation.core.viewmodel)
            implementation(projects.module.presentation.core.designsystem)
            implementation(projects.module.presentation.core.appelements)
            implementation(projects.module.presentation.core.utils)

            implementation(projects.module.presentation.navigation.api)

            implementation(projects.module.presentation.favorites.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.favorites.impl"
}
