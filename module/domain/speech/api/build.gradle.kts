plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.domain.speech.model)
            api(projects.module.domain.card.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.speech.api"
}
