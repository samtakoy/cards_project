plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(project(":module:domain:favorites:api"))
            implementation(project(":module:data:common:api"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.favorites.impl"
}
