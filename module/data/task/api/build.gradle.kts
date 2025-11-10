plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.domain.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.task.api"
}
