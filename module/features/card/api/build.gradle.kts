plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.core.cards.model.card)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.card.api"
}
