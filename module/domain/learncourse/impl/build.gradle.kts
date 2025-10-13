plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.learncourse.impl"
}

dependencies {
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:learncourse:api"))
    implementation(project(":module:data:common:api"))
}