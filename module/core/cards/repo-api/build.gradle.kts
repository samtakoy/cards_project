plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.core.cards.model.card)
            implementation(projects.module.core.cards.model.learncourse)
            implementation(projects.module.core.cards.model.qpack)
            implementation(projects.module.core.cards.model.theme)
            implementation(projects.module.core.cards.model.view)
        }
    }
}

android {
    namespace = "ru.samtakoy.core.cards.api"
}