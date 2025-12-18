plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.core.db.model.theme)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.theme.api"
}
