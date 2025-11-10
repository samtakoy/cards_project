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

            implementation(projects.module.data.import.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.importcards.impl"
}