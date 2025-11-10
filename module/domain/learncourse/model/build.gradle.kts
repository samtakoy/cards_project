plugins {
    id("convention.kmp-lib.plugin")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":module:common:utils"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.learncourse.model"
}
