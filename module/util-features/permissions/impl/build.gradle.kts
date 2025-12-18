plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            api(projects.module.utilFeatures.permissions.api)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
    }
}


android {
    namespace = "ru.samtakoy.utilfeatures.permissions.impl"
}
