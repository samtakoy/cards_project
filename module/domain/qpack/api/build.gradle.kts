plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.qpack.api"
}

dependencies {
    implementation(project(":module:domain:qpack:model"))
}