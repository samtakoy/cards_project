plugins {
    id("convention.kmp-compose-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
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