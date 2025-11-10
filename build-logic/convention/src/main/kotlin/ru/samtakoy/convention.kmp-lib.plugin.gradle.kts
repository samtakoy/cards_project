import gradle.kotlin.dsl.accessors._dc12adddf845545d82d0996eeb7ee381.implementation
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

    // Если эти строки закомментированы или отсутствуют, iOS таргета нет
    // iosX64()
    // iosArm64()
    // iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.napier)
        }

        // androidMain автоматически наследует все зависимости из commonMain
        // Здесь добавляются ТОЛЬКО Android-специфичные зависимости
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
