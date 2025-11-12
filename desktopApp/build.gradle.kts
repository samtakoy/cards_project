import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

group = "ru.samtakoy.cards"
version = "1.0.0"

kotlin {
    jvm("desktop") {
        compilerOptions {
            val projectJavaVersion = JavaVersion.toVersion(libs.versions.desktopJavaVersion.get().toInt())
            jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.napier)

                implementation(libs.filekit.core)

                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(projects.module.common.maindi)

                implementation(projects.module.presentation.main.impl)

                implementation(projects.module.common.utils)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.samtakoy.desktop.MainKt" // Измените на ваш пакет, например "com.example.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.samtakoy.cards"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "ru.samtakoy.cards"
            }

            windows {
                menuGroup = "ru.samtakoy.cards"
                upgradeUuid = "ef03d29f-e845-490d-82d2-f22c2aaae7ec" // Сгенерируйте UUID
            }

            linux {
                packageName = "ru.samtakoy.cards"
            }
        }
    }
}
