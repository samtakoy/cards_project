plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.core.db.repoApi)

            implementation(projects.module.features.view.api)
            implementation(projects.module.features.card.api)

            implementation(projects.module.common.utils.common)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.view.impl"
}
