plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin.support)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    id("convention.base.plugin")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
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
    // см. ниже для Compose

    // result api
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)


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

    // koin
    // implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.androidx.workmanager)

    // retrofit
    implementation(libs.bundles.squareup)

    implementation(projects.module.platform.api)
    implementation(projects.module.platform.impl)

    implementation(projects.module.platform.import.implAndroid)
    implementation(projects.module.platform.notification.implAndroid)
    implementation(projects.module.platform.permissions.implAndroid)

    implementation(projects.module.data.common.api)
    implementation(projects.module.data.common.impl)

    implementation(projects.module.data.task.api)
    implementation(projects.module.data.task.impl)

    implementation(projects.module.domain.card.api)
    implementation(projects.module.domain.card.impl)

    implementation(projects.module.domain.learncourse.api)
    implementation(projects.module.domain.learncourse.impl)

    implementation(projects.module.domain.qpack.api)
    implementation(projects.module.domain.qpack.impl)

    implementation(projects.module.domain.theme.api)
    implementation(projects.module.domain.theme.impl)

    implementation(projects.module.domain.view.api)
    implementation(projects.module.domain.view.impl)

    implementation(projects.module.domain.favorites.api)
    implementation(projects.module.domain.favorites.impl)

    implementation(projects.module.data.import.api)
    implementation(projects.module.data.import.impl)

    implementation(projects.module.domain.export.api)
    implementation(projects.module.domain.import.api)
    implementation(projects.module.domain.import.impl)

    implementation(projects.module.presentation.core.utils)
    implementation(projects.module.presentation.core.viewmodel)
    implementation(projects.module.presentation.core.designsystem)

    implementation(projects.module.presentation.navigation.api)

    implementation(projects.module.presentation.main.api)
    implementation(projects.module.presentation.main.impl)

    implementation(projects.module.presentation.themes.api)
    implementation(projects.module.presentation.themes.impl)

    implementation(projects.module.presentation.qpacks.api)
    implementation(projects.module.presentation.qpacks.impl)

    implementation(projects.module.presentation.cards.api)
    implementation(projects.module.presentation.cards.impl)

    implementation(projects.module.presentation.settings.api)
    implementation(projects.module.presentation.settings.impl)

    implementation(projects.module.presentation.courses.api)
    implementation(projects.module.presentation.courses.impl)

    implementation(projects.module.presentation.viewshistory.api)
    implementation(projects.module.presentation.viewshistory.impl)

    implementation(projects.module.presentation.favorites.api)
    implementation(projects.module.presentation.favorites.impl)

    implementation(projects.module.common.utils)
    implementation(projects.module.common.oldresources)
}