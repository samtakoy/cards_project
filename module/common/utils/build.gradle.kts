plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.common.utils"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    api(libs.gson)

    implementation(project(":module:platform:api"))
}