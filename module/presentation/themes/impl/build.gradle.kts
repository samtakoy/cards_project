plugins {
    id("convention.kmp-compose-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            // implementation(compose.runtime) in convention plugin
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.navigation.compose)
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // filekit
            implementation(libs.filekit.dialogs.compose)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.platform.permissions.api)

            implementation(projects.module.domain.task.model)

            implementation(projects.module.domain.export.api)
            implementation(projects.module.features.import.api)
            implementation(projects.module.domain.theme.api)
            implementation(projects.module.domain.qpack.api)

            implementation(projects.module.presentation.core.viewmodel)
            implementation(projects.module.presentation.core.designsystem)
            implementation(projects.module.presentation.core.appelements)
            implementation(projects.module.presentation.core.utils)

            implementation(projects.module.presentation.navigation.api)

            implementation(projects.module.presentation.themes.api)
            implementation(projects.module.presentation.qpacks.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.themes.impl"
}
