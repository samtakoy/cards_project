plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(projects.module.common.utils.common)
        }
    }
}

android {
    namespace = "ru.samtakoy.core.log"
}