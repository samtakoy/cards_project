import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("kotlin-kapt") // TODO ksp
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.gradle)
}

android {
    namespace = "ru.samtakoy.data.impl"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    // room
    // TODO ksp
    kapt(libs.androidx.room.compiler)
    implementation(libs.bundles.room)
    implementation(libs.kotlinx.coroutines.core)

    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    implementation(project(":module:common:utils"))
    implementation(project(":module:platform:impl"))
    implementation(project(":module:data:common:api"))

    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:view:model"))
}