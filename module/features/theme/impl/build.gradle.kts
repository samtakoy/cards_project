plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.features.theme.api)
            implementation(projects.module.features.qpack.api)
            implementation(projects.module.core.db.repoApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.theme.impl"
}
