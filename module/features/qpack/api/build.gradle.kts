plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.module.core.db.model.qpack)
        }
    }
}

android {
    namespace = "ru.samtakoy.featuers.qpack.api"
}
