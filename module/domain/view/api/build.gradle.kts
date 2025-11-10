plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":module:domain:view:model"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.view.api"
}
