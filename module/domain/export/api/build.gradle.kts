plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.exportcards.api"
}

dependencies {
    // @WorkerThread
    implementation(libs.androidx.core.ktx)

    implementation(projects.module.domain.qpack.model)
}