plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.filekit.core)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.domain.repoApi"
}
