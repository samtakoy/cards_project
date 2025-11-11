plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            //
        }
    }
}

android {
    namespace = "ru.samtakoy.platform.permissions.api"
}
