plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.preview)
            implementation(compose.components.uiToolingPreview)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(projects.module.common.resources)

            implementation(projects.module.common.utils)
            implementation(projects.module.domain.learncourse.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.utils"
}
