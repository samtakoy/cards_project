plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.favorites.impl"
}

dependencies {
    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:domain:favorites:api"))
    implementation(project(":module:data:common:api"))
}