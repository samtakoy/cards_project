plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.card.impl"
}

dependencies {
    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:card:api"))
    implementation(project(":module:data:common:api"))
}