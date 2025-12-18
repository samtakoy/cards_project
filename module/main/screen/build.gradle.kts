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
            implementation(libs.androidx.navigation.compose)
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)

            implementation(projects.module.common.viewmodel)
            implementation(projects.module.uikit.designsystem)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.core.navigation.api)

            implementation(projects.module.features.card.presentationApi)
            implementation(projects.module.features.theme.presentationApi)
            implementation(projects.module.features.qpack.presentationApi)
            implementation(projects.module.features.settings.presentationApi)
            implementation(projects.module.features.learncourse.presentationApi)
            implementation(projects.module.features.view.presentationApi)
            implementation(projects.module.features.favorites.presentationApi)

            implementation(projects.module.features.speech.presentation)

            implementation(projects.module.utilFeatures.permissions.impl)
        }
    }
}

android {
    namespace = "ru.samtakoy.main.screen"
    buildFeatures {
        compose = true
    }
}
