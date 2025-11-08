plugins {
    id("convention.android-lib.plugin")
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.samtakoy.presentation.favorites.impl"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(projects.module.common.utils)
    
    implementation(projects.module.presentation.core.viewmodel)
    implementation(projects.module.presentation.core.designsystem)
    implementation(projects.module.presentation.core.appelements)
    implementation(projects.module.presentation.core.utils)

    implementation(projects.module.presentation.navigation.api)

    implementation(projects.module.presentation.favorites.api)
}