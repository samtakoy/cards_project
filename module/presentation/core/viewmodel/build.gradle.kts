plugins {
    alias(libs.plugins.compose.compiler)
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.presentation.core.viewmodel"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // for fragment ext + android viewmodel
    implementation(libs.androidx.appcompat)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(project(":module:common:utils"))
}