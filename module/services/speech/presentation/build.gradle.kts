plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(compose.ui)
            implementation(compose.material3)
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)

            implementation(projects.module.common.viewmodel)
            implementation(projects.module.core.uikit)
            implementation(projects.module.common.utils.presentation)

            implementation(projects.module.services.speech.api)
        }
    }
}
android {
    namespace = "ru.samtakoy.speech.presentation"
}
