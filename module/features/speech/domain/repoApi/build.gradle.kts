plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.features.speech.domain.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.domain.repoapi"
}
