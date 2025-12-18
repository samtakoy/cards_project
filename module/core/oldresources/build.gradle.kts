plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.common.oldresources"
}

dependencies {
    // koin
    implementation(libs.koin.core)
}