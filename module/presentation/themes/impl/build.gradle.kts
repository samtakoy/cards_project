plugins {
    id("convention.android-lib.plugin")
    id("kotlin-parcelize")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.samtakoy.presentation.themes.impl"
}

dependencies {
    // Compose
    implementation(libs.androidx.navigation.compose)
    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.material3)
    implementation(compose.preview)
    implementation(compose.materialIconsExtended)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    // filekit
    implementation(libs.filekit.dialogs.compose)

    implementation(projects.module.common.utils)
    implementation(projects.module.common.resources)

    implementation(projects.module.platform.import.api)
    implementation(projects.module.platform.permissions.api)

    implementation(projects.module.domain.task.model)

    implementation(projects.module.domain.export.api)
    implementation(projects.module.domain.import.api)
    implementation(projects.module.domain.theme.api)
    implementation(projects.module.domain.qpack.api)

    implementation(projects.module.presentation.core.viewmodel)
    implementation(projects.module.presentation.core.designsystem)
    implementation(projects.module.presentation.core.appelements)
    implementation(projects.module.presentation.core.utils)

    implementation(projects.module.presentation.navigation.api)

    implementation(projects.module.presentation.themes.api)
    implementation(projects.module.presentation.qpacks.api)
}