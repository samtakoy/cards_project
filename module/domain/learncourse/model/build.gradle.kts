plugins {
    id("convention.android-lib.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.samtakoy.domain.learncourse.model"
}

dependencies {
    implementation(project(":module:common:utils"))
}