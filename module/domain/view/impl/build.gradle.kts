plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // koin
            implementation(libs.koin.core)

            implementation(projects.module.domain.commonRepoApi)

            implementation(projects.module.domain.view.api)
            implementation(projects.module.domain.card.api)

            implementation(projects.module.common.utils)
        }
    }
}

android {
    namespace = "ru.samtakoy.domain.view.impl"
}
