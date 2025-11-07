plugins {
    id("convention.android-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.samtakoy.presentation.cards.api"
}

dependencies {
    implementation(libs.kotlinx.serialization.core)

    implementation(projects.module.presentation.navigation.api)
}