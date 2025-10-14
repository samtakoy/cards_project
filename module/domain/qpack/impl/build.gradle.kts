plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.qpack.impl"
}

dependencies {
    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:qpack:api"))
    // implementation(project(":module:domain:view:api"))

    implementation(project(":module:data:common:api"))
    // implementation(project(":module:platform:api"))
}