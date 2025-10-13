plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.common.utils"
}

dependencies {
    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    api(libs.gson)

    implementation(project(":module:platform:impl"))
}