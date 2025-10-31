plugins {
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.presentation.main.api"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
}