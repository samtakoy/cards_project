plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":module:domain:qpack:model"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.qpack.api"
}
