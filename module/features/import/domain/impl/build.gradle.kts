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

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.data.common.api)

            implementation(projects.module.features.import.domain.repoApi)
            implementation(projects.module.features.import.domain.api)

            implementation(projects.module.domain.task.model)

            implementation(projects.module.domain.card.api)
            implementation(projects.module.domain.qpack.api)
            implementation(projects.module.domain.theme.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.importcards.domain.impl"
}
