plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.permissions.impl_android"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(libs.androidx.appcompat)

    api(projects.module.platform.permissions.api)
}