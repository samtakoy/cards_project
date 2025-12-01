plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.features.speech.domain.model)
            api(projects.module.domain.card.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.domain.api"
}
