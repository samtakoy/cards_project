plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.qpack.impl"
}

dependencies {
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:qpack:api"))
    implementation(project(":module:data:common:api"))
}