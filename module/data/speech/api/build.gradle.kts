plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.domain.speech.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.speech.api"
}
