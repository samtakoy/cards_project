plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.task.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(projects.module.data.task.api)
    implementation(projects.module.domain.task.model)
}