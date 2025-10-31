plugins {
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.samtakoy.presentation.core.appelements"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(projects.module.presentation.core.utils)
    implementation(projects.module.presentation.core.designsystem)
}