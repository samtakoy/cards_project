plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.domain.favorites.api)
            implementation(projects.module.domain.commonRepoApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.favorites.impl"
}
