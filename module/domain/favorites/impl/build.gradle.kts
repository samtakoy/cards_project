plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.favorites.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(project(":module:domain:favorites:api"))
    implementation(project(":module:data:common:api"))
}