plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit
            implementation(libs.filekit.dialogs.compose)

            api(projects.module.domain.card.model)
            api(projects.module.domain.qpack.model)
            api(projects.module.domain.theme.model)
            api(projects.module.domain.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.api"
}
