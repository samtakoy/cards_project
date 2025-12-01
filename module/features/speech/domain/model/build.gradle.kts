plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            api(libs.koin.core)

            implementation(projects.module.common.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.domain.model"
}
