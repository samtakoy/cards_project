plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":module:domain:card:model"))
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.card.api"
}
