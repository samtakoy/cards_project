plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.card.impl"
}

dependencies {
    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:card:api"))
    implementation(project(":module:data:common:api"))
}