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
            implementation(projects.module.core.uikit)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.core.navigation.api)

            implementation(projects.module.main.tabnavigation.api)

            implementation(projects.module.features.learncourse.presentationApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.learncourse.presentation.impl"
}
