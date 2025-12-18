plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.filekit.core)
            }
        }
    }
}

android {
    namespace = "ru.samtakoy.utilfeatures.download.api"
}