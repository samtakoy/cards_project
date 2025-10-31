plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.importcards.impl"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.kmpio)
    // implementation(libs.kmpio.zip)
    implementation(libs.filekit.core)

    implementation(projects.module.data.import.api)
}