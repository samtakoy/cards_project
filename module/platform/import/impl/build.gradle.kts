plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            // filekit
            implementation(libs.filekit.dialogs.compose)

            implementation(projects.module.platform.import.api)

            implementation(projects.module.data.task.api)
            implementation(projects.module.domain.task.model)

            implementation(projects.module.domain.import.api)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)
            // worker
            implementation(libs.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.guava)

            //
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.module.platform.notification.impl)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)
        }
    }
}

android {
    namespace = "ru.samtakoy.platform.importcards.implandroid"
}
