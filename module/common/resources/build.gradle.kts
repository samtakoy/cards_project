plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            // для AnnotatedString
            api(compose.foundation)
            api(compose.components.resources)
        }
    }
}

android {
    namespace = "ru.samtakoy.common.resources"
    buildFeatures {
        compose = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "ru.samtakoy.resources"
    generateResClass = always
}