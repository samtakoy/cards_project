plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.text.to.speech)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)

            implementation(projects.module.core.cards.repoApi)

            implementation(projects.module.services.speech.api)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)
            // worker
            implementation(libs.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.guava)

            //
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.module.services.notification.impl)


        }
        desktopMain.dependencies {
        }
    }
}

android {
    namespace = "ru.samtakoy.services.speech.impl"
}