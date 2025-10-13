plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.platform.impl"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    // implementation(libs.androidx.appcompat)

    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:platform:api"))
}