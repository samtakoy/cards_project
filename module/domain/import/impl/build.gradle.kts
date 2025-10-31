plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.importcards.impl"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    // filekit
    implementation(libs.filekit.dialogs.compose)

    implementation(projects.module.common.utils)

    implementation(projects.module.data.common.api)

    implementation(projects.module.data.import.api)
    implementation(projects.module.domain.import.api)
    implementation(projects.module.domain.import.model)

    implementation(projects.module.domain.task.model)

    implementation(projects.module.domain.card.model)
    implementation(projects.module.domain.card.api)
    implementation(projects.module.domain.qpack.model)
    implementation(projects.module.domain.qpack.api)
    implementation(projects.module.domain.theme.model)
    implementation(projects.module.domain.theme.api)
}