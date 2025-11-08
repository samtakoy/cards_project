plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.importcards.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    // filekit
    implementation(libs.filekit.dialogs.compose)

    implementation(projects.module.common.utils)
    implementation(projects.module.common.resources)

    implementation(projects.module.data.common.api)

    implementation(projects.module.data.import.api)
    implementation(projects.module.domain.import.api)

    implementation(projects.module.domain.task.model)

    implementation(projects.module.domain.card.api)
    implementation(projects.module.domain.qpack.api)
    implementation(projects.module.domain.theme.api)
}