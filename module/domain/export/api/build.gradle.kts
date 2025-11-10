plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.domain.qpack.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.exportcards.api"
}
