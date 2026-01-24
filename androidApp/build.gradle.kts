import ru.samtakoy.ext.desktopJavaVersionInt
import ru.samtakoy.ext.projectJavaVersion
import ru.samtakoy.ext.projectJavaVersionInt

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


kotlin {
    jvmToolchain(projectJavaVersionInt)
}

android {
    namespace = "ru.samtakoy"
    buildFeatures {
        // TODO remove
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
        debug {
            isDebuggable = true
            isMinifyEnabled = false
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

    // rxjava
    implementation(libs.reactivex.rxjava)
    implementation(libs.reactivex.rxjava.rxandroid)
    implementation(libs.reactivex.rxjava.coroutines)

    // log
    implementation(libs.napier)

    // koin
    // implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.androidx.workmanager)

    // retrofit
    implementation(libs.bundles.squareup)

    implementation(projects.module.core.log)

    implementation(projects.module.main.di)

    implementation(projects.module.services.permissions.impl)

    implementation(projects.module.core.cards.repoApi)

    implementation(projects.module.services.task.api)

    implementation(projects.module.features.card.api)

    implementation(projects.module.features.learncourse.api)

    implementation(projects.module.features.qpack.api)

    implementation(projects.module.features.theme.api)

    implementation(projects.module.features.view.api)

    implementation(projects.module.features.favorites.api)

    implementation(projects.module.services.export.api)
    implementation(projects.module.services.import.api)

    implementation(projects.module.common.utils.presentation)
    implementation(projects.module.common.viewmodel)
    implementation(projects.module.core.uikit)

    implementation(projects.module.core.navigation.api)

    implementation(projects.module.main.screen)

    implementation(projects.module.features.theme.presentationApi)

    implementation(projects.module.features.qpack.presentationApi)
    implementation(projects.module.features.qpack.presentationImpl)

    implementation(projects.module.features.card.presentationApi)

    implementation(projects.module.features.settings.presentationApi)

    implementation(projects.module.features.learncourse.presentationApi)
    implementation(projects.module.features.learncourse.presentationImpl)

    implementation(projects.module.features.view.presentationApi)
    implementation(projects.module.features.view.presentationImpl)

    implementation(projects.module.features.favorites.presentationApi)
    implementation(projects.module.features.favorites.presentationImpl)

    implementation(projects.module.common.utils.common)
    implementation(projects.module.core.oldresources)
}