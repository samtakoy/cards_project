plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.importcards.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(libs.kmpio)
    implementation(libs.filekit.core)

    implementation(projects.module.data.import.api)
}