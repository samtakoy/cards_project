plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.task.api"
}

dependencies {
    implementation(projects.module.domain.task.model)
}