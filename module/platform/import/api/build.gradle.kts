plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit (for PlatformFile)
            implementation(libs.filekit.dialogs.compose)

            implementation(projects.module.domain.task.model)
            implementation(projects.module.domain.import.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.platform.importcards.api"
}

