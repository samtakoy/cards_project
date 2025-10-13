plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.api"
}

dependencies {
    implementation(project(":module:domain:theme:model"))
}