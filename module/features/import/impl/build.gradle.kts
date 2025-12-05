plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(libs.kmpio)
            // filekit
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs.compose)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.domain.commonRepoApi)
            implementation(projects.module.data.task.api)

            implementation(projects.module.features.import.api)

            implementation(projects.module.domain.task.model)

            implementation(projects.module.domain.card.api)
            implementation(projects.module.domain.qpack.api)
            implementation(projects.module.domain.theme.api)
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
    namespace = "ru.samtakoy.features.importcards.impl"
}
