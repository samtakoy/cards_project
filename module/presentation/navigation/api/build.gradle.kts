plugins {
    // alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.samtakoy.presentation.navigation.api"
    /*
    buildFeatures {
        compose = true
    }*/
}

dependencies {
    implementation(libs.kotlinx.serialization.core)
    // Compose
    // implementation(platform(libs.androidx.compose.bom))
    // implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
}