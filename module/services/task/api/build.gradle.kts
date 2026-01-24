plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.services.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.services.task.api"
}
