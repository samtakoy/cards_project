plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.presentation.core.utils"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(libs.androidx.navigation.compose)
    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.material3)
    implementation(compose.preview)

    implementation(projects.module.common.resources)

    implementation(projects.module.common.utils)
    implementation(projects.module.domain.learncourse.model)
}