plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.data.task.api)
            implementation(projects.module.domain.task.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.task.impl"
}
