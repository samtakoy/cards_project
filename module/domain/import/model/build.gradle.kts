plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.importcards.model"
}

dependencies {
    // filekit
    implementation(libs.filekit.dialogs.compose)
}