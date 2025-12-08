plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "ru.samtakoy.common.utils"
}