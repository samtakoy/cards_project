plugins {
    id("kotlin-kapt") // TODO ksp
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.gradle)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("kotlin-parcelize")
}

android {
    namespace = "ru.samtakoy"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
        compose = true
    }
    defaultConfig {
        applicationId = "ru.samtakoy"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                        // here go the options for Moxy compiler
                        "defaultMoxyStrategy" to "moxy.viewstate.strategy.AddToEndSingleStrategy",
                        "room.schemaLocation" to "$projectDir/schemas".toString()
                )
            }
        }/**/
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference)

    implementation(libs.androidx.core.ktx)

    // (Java only)
    implementation(libs.androidx.work)

    // TODO ksp
    kapt(libs.androidx.room.compiler)

    // navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // result api
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // room
    implementation(libs.bundles.room)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // misc
    implementation(libs.android.support.multidex)
    implementation(libs.google.android.material)

    implementation(libs.apache.commons)

    // Coroutines
    implementation(libs.bundles.kotlinx)

    // Stetho core
    implementation(libs.facebook.stetho)
    //If you want to add a network helper
    // implementation("com.facebook.stetho:stetho-okhttp:1.5.1")

    // rxjava
    implementation(libs.reactivex.rxjava)
    implementation(libs.reactivex.rxjava.rxandroid)
    implementation(libs.reactivex.rxjava.coroutines)

    // log
    implementation(libs.jakewharton.timber)

    // dagger
    api(libs.google.dagger)
    annotationProcessor(libs.google.dagger.compiler)
    kapt(libs.google.dagger.compiler)

    // retrofit
    implementation(libs.bundles.squareup)

}



