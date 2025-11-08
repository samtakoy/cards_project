plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.notification.impl_android"
}

dependencies {
    // koin
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.workmanager)

    implementation(projects.module.common.utils)
    implementation(projects.module.common.resources)
}