plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.core.db.model.view)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.view.api"
}
