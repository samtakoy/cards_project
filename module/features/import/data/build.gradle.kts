plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.kmpio)
            implementation(libs.filekit.core)

            implementation(projects.module.features.import.domain.repoApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.data"
}