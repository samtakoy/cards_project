plugins {
    id("convention.android-lib.plugin")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.samtakoy.presentation.main.impl"
    buildFeatures {
        compose = true
    }
}

dependencies {
    // android view-model?
    // implementation(libs.androidx.appcompat)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(projects.module.common.utils)

    implementation(projects.module.presentation.core.viewmodel)
    implementation(projects.module.presentation.core.designsystem)
    implementation(projects.module.presentation.core.utils)

    implementation(projects.module.presentation.navigation.api)

    implementation(projects.module.presentation.themes.api)
    implementation(projects.module.presentation.qpacks.api)
    implementation(projects.module.presentation.settings.api)
    implementation(projects.module.presentation.courses.api)
    implementation(projects.module.presentation.viewshistory.api)
    implementation(projects.module.presentation.favorites.api)

    implementation(projects.module.platform.permissions.implAndroid)
}