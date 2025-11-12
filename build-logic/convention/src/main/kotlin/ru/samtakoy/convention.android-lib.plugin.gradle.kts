import ru.samtakoy.ext.desktopJavaVersionInt
import ru.samtakoy.ext.libs
import ru.samtakoy.ext.projectJavaVersion
import ru.samtakoy.ext.projectJavaVersionInt

plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("kotlin-android")
    id("convention.base.plugin")
}

kotlin {
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
    }
}

dependencies {
    implementation(libs.bundles.kotlinx.pure)

    implementation(libs.napier)
}
