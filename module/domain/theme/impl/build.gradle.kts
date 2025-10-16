plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.theme.impl"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:theme:api"))
    implementation(project(":module:domain:qpack:api"))
    implementation(project(":module:data:common:api"))
}