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

            implementation(projects.module.data.speech.api)
            implementation(projects.module.data.common.api)


            implementation(projects.module.domain.speech.api)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.speech.impl"
}