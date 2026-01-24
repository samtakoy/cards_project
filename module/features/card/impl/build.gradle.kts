plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.features.card.api)
            implementation(projects.module.core.cards.repoApi)
        }
    }
}

android {
    namespace = "ru.samtakoy.features.card.impl"
}
