plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(libs.gson)
        }
    }
}

android {
    namespace = "ru.samtakoy.common.utils"
}