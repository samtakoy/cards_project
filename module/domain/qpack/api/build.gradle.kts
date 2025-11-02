plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.qpack.api"
}

dependencies {
    api(project(":module:domain:qpack:model"))
}