plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // filekit
            implementation(libs.filekit.dialogs.compose)
        }
     }
}

android {
    namespace = "ru.samtakoy.domain.importcards.model"
}