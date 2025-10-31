plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.importcards.api"
}

dependencies {
    // filekit
    implementation(libs.filekit.dialogs.compose)

    implementation(projects.module.domain.task.model)
    implementation(projects.module.domain.import.model)
}
