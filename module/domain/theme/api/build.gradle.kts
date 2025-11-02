plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.api"
}

dependencies {
    api(project(":module:domain:theme:model"))
}