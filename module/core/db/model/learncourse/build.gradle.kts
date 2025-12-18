plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)

            implementation(projects.module.common.utils.common)
        }
    }
}

android {
    namespace = "ru.samtakoy.core.db.model.learncourse"
}
