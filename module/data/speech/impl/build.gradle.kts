plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.text.to.speech)

            implementation(projects.module.data.speech.api)
            implementation(projects.module.domain.speech.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.speech.impl"
}