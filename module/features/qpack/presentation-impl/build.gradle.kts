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
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)

            implementation(projects.module.features.card.api)
            implementation(projects.module.features.qpack.api)
            implementation(projects.module.features.favorites.api)
            implementation(projects.module.features.learncourse.api)
            implementation(projects.module.features.view.api)

            implementation(projects.module.common.viewmodel)
            implementation(projects.module.core.uikit)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.core.navigation.api)

            implementation(projects.module.main.tabnavigation.api)

            implementation(projects.module.features.card.presentationApi)
            implementation(projects.module.features.qpack.presentationApi)
            implementation(projects.module.services.speech.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.qpack.presentation.impl"
}
