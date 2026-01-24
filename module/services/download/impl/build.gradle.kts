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

		        implementation(projects.module.services.download.api)

		        implementation(projects.module.core.network)
            }
        }
    }
}

android {
    namespace = "ru.samtakoy.services.download.impl"
}