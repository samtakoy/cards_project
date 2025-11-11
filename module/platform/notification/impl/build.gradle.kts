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

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)
        }
    }
}
android {
    namespace = "ru.samtakoy.platform.notification.impl"
}
