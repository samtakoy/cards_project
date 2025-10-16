import ru.samtakoy.ext.libs

plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("kotlin-android")
    // id("kotlin-parcelize")
    id("convention.base.plugin")
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

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jakewharton.timber)
}
