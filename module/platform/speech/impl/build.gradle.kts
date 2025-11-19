import org.gradle.kotlin.dsl.androidMain

plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.domain.speech.api)
            implementation(projects.module.platform.speech.api)
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
    namespace = "ru.samtakoy.platform.speech.impl"
}
