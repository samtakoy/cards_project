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

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)

            implementation(projects.module.core.navigation.api)
            implementation(projects.module.main.tabnavigation.api)

            implementation(projects.module.services.permissions.api)

            implementation(projects.module.services.task.model)

            implementation(projects.module.services.export.api)
            implementation(projects.module.services.import.api)
            implementation(projects.module.services.download.api)
            implementation(projects.module.features.theme.api)
            implementation(projects.module.features.qpack.api)

            implementation(projects.module.common.viewmodel)
            implementation(projects.module.core.uikit)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.features.theme.presentationApi)
            implementation(projects.module.features.qpack.presentationApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.theme.presentation.impl"
}