plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.common.utils"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    api(libs.gson)
}