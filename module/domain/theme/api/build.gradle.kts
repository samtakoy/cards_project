plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":module:domain:theme:model"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.theme.api"
}
