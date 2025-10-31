plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.importcards.api"
}

dependencies {
    // filekit
    implementation(libs.filekit.dialogs.compose)

    implementation(projects.module.domain.card.model)
    implementation(projects.module.domain.qpack.model)
    implementation(projects.module.domain.theme.model)

    implementation(projects.module.domain.task.model)

    implementation(projects.module.domain.import.model)
}