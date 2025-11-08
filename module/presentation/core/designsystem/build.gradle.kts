plugins {
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.samtakoy.presentation.core.designsystem"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.material.icons.extended)

    implementation(projects.module.presentation.core.utils)
}