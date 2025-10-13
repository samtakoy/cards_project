plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.view.impl"
}

dependencies {
    implementation(project(":module:domain:view:model"))
    implementation(project(":module:domain:view:api"))
    implementation(project(":module:data:common:api"))
}