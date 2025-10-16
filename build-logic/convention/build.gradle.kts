plugins {
    // чтобы использовать Gradle Kotlin Dsl для написания наших Convention Plugins;
    `kotlin-dsl`
}

//  плагины, которые будем использовать в наших будущих Convention Plugins.
dependencies {
    implementation(libs.gradleplugin.kotlin)
    implementation(libs.gradleplugin.android)
    implementation(libs.gradleplugin.ksp)
    // Workaround for version catalog working inside precompiled scripts
    // Issue - https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}