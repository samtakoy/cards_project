plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit
            implementation(libs.filekit.dialogs.compose)

            api(projects.module.core.cards.model.card)
            api(projects.module.core.cards.model.qpack)
            api(projects.module.core.cards.model.theme)
            api(projects.module.services.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.api"
}
