plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // ViewModel
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            // Lifecycle
            implementation(libs.lifecycle.runtime.compose)

            implementation(projects.module.common.utils.common)

        }
    }
}

android {
    namespace = "ru.samtakoy.common.viewmodel"
}