plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.services.task.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.services.task.impl"
}
