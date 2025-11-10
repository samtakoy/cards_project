plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":module:domain:learncourse:model"))
            api(project(":module:domain:view:model"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.learncourse.api"
}
