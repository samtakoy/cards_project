import ru.samtakoy.ext.libs
import ru.samtakoy.ext.projectJavaVersion

plugins {
    id("convention.kmp-lib.plugin")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
