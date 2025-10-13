plugins {
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.presentation.utils"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(project(":module:platform:api"))
    implementation(project(":module:common:utils"))
    implementation(project(":module:domain:learncourse:model"))
}