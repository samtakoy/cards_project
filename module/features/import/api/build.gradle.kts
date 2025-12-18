plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit
            implementation(libs.filekit.dialogs.compose)

            api(projects.module.core.db.model.card)
            api(projects.module.core.db.model.qpack)
            api(projects.module.core.db.model.theme)
            api(projects.module.utilFeatures.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.api"
}
