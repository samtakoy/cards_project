plugins {
    id("kotlin-kapt") // TODO ksp
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin.support)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("kotlin-parcelize")
    id("convention.base.plugin")
}

android {
    namespace = "ru.samtakoy"
    buildFeatures {
        viewBinding = true
        compose = true
    }
    defaultConfig {
        applicationId = "ru.samtakoy"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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

    // navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // result api
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // room
    // TODO ksp
    kapt(libs.androidx.room.compiler)
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

    implementation(project(":module:common:utils"))

    implementation(project(":module:presentation:utils"))

    implementation(project(":module:platform:api"))
    implementation(project(":module:platform:impl"))

    implementation(project(":module:domain:card:api"))
    implementation(project(":module:domain:card:model"))
    implementation(project(":module:domain:card:impl"))

    implementation(project(":module:domain:learncourse:api"))
    implementation(project(":module:domain:learncourse:model"))
    implementation(project(":module:domain:learncourse:impl"))

    implementation(project(":module:domain:qpack:api"))
    implementation(project(":module:domain:qpack:model"))
    implementation(project(":module:domain:qpack:impl"))

    implementation(project(":module:domain:theme:api"))
    implementation(project(":module:domain:theme:model"))
    implementation(project(":module:domain:theme:impl"))

    implementation(project(":module:domain:view:api"))
    implementation(project(":module:domain:view:model"))
    implementation(project(":module:domain:view:impl"))

    implementation(project(":module:data:common:api"))
    implementation(project(":module:data:common:impl"))
}