plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)
        }
    }
}
android {
    namespace = "ru.samtakoy.utilfeatures.notification.impl"
}
