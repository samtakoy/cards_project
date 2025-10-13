plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.api"
}

dependencies {
    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:view:model"))
}