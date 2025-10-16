plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.favorites.impl"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":module:domain:favorites:api"))
    implementation(project(":module:data:common:api"))
}