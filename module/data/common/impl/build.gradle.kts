plugins {
    id("convention.android-lib.plugin")
}

android {
    namespace = "ru.samtakoy.data.impl"
}

dependencies {
    // room
    // TODO ksp
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":module:common:utils"))
    implementation(project(":module:platform:api"))
    implementation(project(":module:data:common:api"))

    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:view:model"))
}