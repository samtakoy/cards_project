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

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.resources)
            implementation(projects.module.core.cards.repoApi)
            implementation(projects.module.services.task.api)

            implementation(projects.module.services.import.api)
            implementation(projects.module.features.card.api)
            implementation(projects.module.features.qpack.api)
            implementation(projects.module.features.theme.api)
        }
        androidMain.dependencies {
            implementation(libs.koin.androidx.workmanager)
            // worker
            implementation(libs.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.guava)
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.module.services.notification.impl)
        }
        desktopMain.dependencies {
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.impl"
}
