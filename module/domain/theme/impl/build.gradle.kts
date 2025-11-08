plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(project(":module:domain:theme:api"))
    implementation(project(":module:domain:qpack:api"))
    implementation(project(":module:data:common:api"))
}