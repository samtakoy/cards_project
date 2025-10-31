plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.impl"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":module:platform:api"))
}