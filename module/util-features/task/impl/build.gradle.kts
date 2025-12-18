plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.utilFeatures.task.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.utilfeatures.task.impl"
}
