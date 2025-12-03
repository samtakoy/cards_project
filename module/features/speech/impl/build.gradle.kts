plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.text.to.speech)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.data.common.api)

            implementation(projects.module.features.speech.api)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)
            // worker
            implementation(libs.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.guava)

            //
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.module.platform.notification.impl)


        }
        desktopMain.dependencies {
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.impl"
}