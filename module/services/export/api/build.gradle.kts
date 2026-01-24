plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.core.cards.model.qpack)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.exportcards.api"
}
