plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // koin
                implementation(libs.koin.core)

                implementation(libs.ktorfit)
                implementation(libs.filekit.core)

		        implementation(projects.module.features.download.api)

		        implementation(projects.module.data.remote)
            }
        }
    }
}

android {
    namespace = "ru.samtakoy.download.impl"
}