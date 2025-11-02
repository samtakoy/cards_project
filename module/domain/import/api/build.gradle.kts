plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.importcards.api"
}

dependencies {
    // filekit
    implementation(libs.filekit.dialogs.compose)

    api(projects.module.domain.card.model)
    api(projects.module.domain.qpack.model)
    api(projects.module.domain.theme.model)
    api(projects.module.domain.task.model)
    api(projects.module.domain.import.model)
}