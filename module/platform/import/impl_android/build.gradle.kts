plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.importcards.implandroid"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.workmanager)

    // worker
    implementation(libs.work.runtime.ktx)
    implementation(libs.kotlinx.coroutines.guava)

    // filekit
    implementation(libs.filekit.dialogs.compose)

    //
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.module.platform.notification.implAndroid)

    implementation(projects.module.common.utils)

    implementation(projects.module.platform.import.api)
    implementation(projects.module.platform.import.impl)

    implementation(projects.module.data.task.api)
    implementation(projects.module.domain.task.model)

    implementation(projects.module.domain.import.api)
}