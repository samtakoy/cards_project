plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            api(projects.module.core.db.model.card)

            implementation(projects.module.common.utils.common)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.api"
}
