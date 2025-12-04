plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            api(projects.module.domain.card.model)

            implementation(projects.module.common.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.api"
}
