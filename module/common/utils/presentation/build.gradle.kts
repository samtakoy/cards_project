plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(projects.module.core.resources)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.cards.model.learncourse)
        }
    }
}

android {
    namespace = "ru.samtakoy.common.utils.presentation"
}
