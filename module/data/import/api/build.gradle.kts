plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.importcards.api"
}

dependencies {
    implementation(libs.filekit.core)
}