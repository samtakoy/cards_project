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
            implementation(compose.preview)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.navigation.compose)
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.domain.card.api)
            implementation(projects.module.domain.qpack.api)
            implementation(projects.module.domain.favorites.api)
            implementation(projects.module.domain.learncourse.api)
            implementation(projects.module.domain.view.api)

            implementation(projects.module.presentation.core.viewmodel)
            implementation(projects.module.presentation.core.designsystem)
            implementation(projects.module.presentation.core.appelements)
            implementation(projects.module.presentation.core.utils)

            implementation(projects.module.presentation.navigation.api)
            implementation(projects.module.presentation.cards.api)

            implementation(projects.module.presentation.qpacks.api)

            implementation(projects.module.features.speech.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.qpacks.impl"
}
