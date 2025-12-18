plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.utilFeatures.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.utilfeatures.task.api"
}
