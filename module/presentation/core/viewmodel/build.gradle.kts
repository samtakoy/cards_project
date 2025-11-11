plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // FlowExt - для прослушивания
            implementation(compose.runtime)

            // ViewModel
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            implementation(projects.module.common.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.viewmodel"
}