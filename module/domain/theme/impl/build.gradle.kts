plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.impl"
}

dependencies {
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:theme:api"))
    implementation(project(":module:data:common:api"))
}