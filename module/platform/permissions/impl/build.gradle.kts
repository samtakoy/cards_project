plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            api(projects.module.platform.permissions.api)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
    }
}


android {
    namespace = "ru.samtakoy.platform.permissions.impl"
}
