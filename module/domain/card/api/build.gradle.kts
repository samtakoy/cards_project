plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.card.api"
}

dependencies {
    api(project(":module:domain:card:model"))
}