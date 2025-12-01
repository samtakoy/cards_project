plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.text.to.speech)

            implementation(projects.module.common.utils)

            implementation(projects.module.features.speech.domain.repoApi)
            implementation(projects.module.features.speech.domain.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.data"
}