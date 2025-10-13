plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.impl"
}

dependencies {
    // room
    // TODO ksp
    kapt(libs.androidx.room.compiler)
    implementation(libs.bundles.room)

    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:common:utils"))
    implementation(project(":module:platform:impl"))
    implementation(project(":module:data:common:api"))

    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:view:model"))
}