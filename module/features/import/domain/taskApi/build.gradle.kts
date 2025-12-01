plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit (for PlatformFile)
            implementation(libs.filekit.dialogs.compose)

            implementation(projects.module.domain.task.model)
            implementation(projects.module.features.import.domain.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.domain.taskApi"
}

