plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
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

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // ?
            // implementation(libs.koin.compose.viewmodel.navigation)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.presentation.core.viewmodel)
            implementation(projects.module.presentation.core.designsystem)
            implementation(projects.module.presentation.core.utils)

            implementation(projects.module.presentation.navigation.api)

            implementation(projects.module.presentation.themes.api)
            implementation(projects.module.presentation.qpacks.api)
            implementation(projects.module.presentation.settings.api)
            implementation(projects.module.presentation.courses.api)
            implementation(projects.module.presentation.viewshistory.api)
            implementation(projects.module.presentation.favorites.api)

            implementation(projects.module.platform.permissions.impl)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.main.impl"
    buildFeatures {
        compose = true
    }
}
