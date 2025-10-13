plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.card.api"
}

dependencies {
    implementation(project(":module:domain:card:model"))
}