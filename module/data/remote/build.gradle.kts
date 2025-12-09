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

                api(libs.ktorfit)
            }
        }
    }
}

android {
    namespace = "ru.samtakoy.data.remote"
}