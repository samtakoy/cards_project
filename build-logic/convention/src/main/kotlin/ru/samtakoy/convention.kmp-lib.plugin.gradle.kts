import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.samtakoy.ext.desktopJavaVersion
import ru.samtakoy.ext.libs
import ru.samtakoy.ext.projectJavaVersion
import ru.samtakoy.ext.projectJavaVersionInt

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("convention.base.plugin")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(desktopJavaVersion.toString()))
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)
            // implementation(libs.kotlinx.io.core) ?

            implementation(libs.napier)
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        /*
        iosMain.dependencies {
            // iOS специфичные libs (если нужны)
        }*/
    }

    jvmToolchain(projectJavaVersionInt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
        }
    }
}