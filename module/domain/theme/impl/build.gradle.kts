plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.impl"
}

dependencies {
    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:theme:api"))
    implementation(project(":module:domain:qpack:api"))
    implementation(project(":module:data:common:api"))
}