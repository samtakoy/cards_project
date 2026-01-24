plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            api(projects.module.services.permissions.api)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
    }
}


android {
    namespace = "ru.samtakoy.services.permissions.impl"
}
