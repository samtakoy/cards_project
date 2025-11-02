plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.view.api"
}

dependencies {
    api(project(":module:domain:view:model"))
}