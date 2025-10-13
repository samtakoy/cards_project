plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.learncourse.api"
}

dependencies {
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:view:model"))
}