plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.common.utils)
            implementation(projects.module.common.resources)

            implementation(projects.module.features.speech.domain.repoApi)
            implementation(projects.module.data.common.api)

            implementation(projects.module.features.speech.domain.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.speech.domain.impl"
}