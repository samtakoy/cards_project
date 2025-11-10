plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(project(":module:data:common:api"))

            implementation(project(":module:domain:view:api"))
            implementation(project(":module:domain:card:api"))

            implementation(project(":module:common:utils"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.view.impl"
}
