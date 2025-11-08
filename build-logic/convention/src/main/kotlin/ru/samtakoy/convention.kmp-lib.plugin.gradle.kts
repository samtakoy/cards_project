import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.samtakoy.ext.libs
import ru.samtakoy.ext.projectJavaVersion

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

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }
    }
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
    }
}
