plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.qpack.impl"
}

dependencies {
    // koin
    implementation(libs.koin.core)

    implementation(project(":module:domain:qpack:api"))
    // implementation(project(":module:domain:view:api"))

    implementation(project(":module:data:common:api"))
    // implementation(project(":module:platform:api"))
}