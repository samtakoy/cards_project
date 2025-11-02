plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.learncourse.api"
}

dependencies {
    api(project(":module:domain:learncourse:model"))
    api(project(":module:domain:view:model"))
}