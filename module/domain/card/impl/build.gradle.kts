plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.card.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(project(":module:domain:card:api"))
    implementation(project(":module:data:common:api"))
}