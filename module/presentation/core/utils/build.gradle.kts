plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.preview)

            implementation(projects.module.common.resources)

            implementation(projects.module.common.utils)
            implementation(projects.module.domain.learncourse.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.presentation.core.utils"
}
