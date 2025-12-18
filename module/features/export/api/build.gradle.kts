plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.core.db.model.qpack)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.exportcards.api"
}
