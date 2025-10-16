plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.domain.view.impl"
}

dependencies {
    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":module:data:common:api"))
    implementation(project(":module:data:common:api"))

    implementation(project(":module:domain:view:model"))
    implementation(project(":module:domain:view:api"))
    implementation(project(":module:domain:card:api"))

    implementation(project(":module:common:utils"))
}