import org.gradle.kotlin.dsl.androidMain

plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.features.speech.domain.api)
            implementation(projects.module.features.speech.domain.taskApi)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)
            // worker
            implementation(libs.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.guava)

            //
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.module.platform.notification.impl)

            implementation(projects.module.common.resources)
            implementation(projects.module.common.utils)
        }
        desktopMain.dependencies {
            implementation(projects.module.common.resources)
            implementation(projects.module.common.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.platform"
}
