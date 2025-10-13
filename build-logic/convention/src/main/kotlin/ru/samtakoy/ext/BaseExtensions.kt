package ru.samtakoy.ext

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

fun Project.withVersionCatalog(block: (libs: LibrariesForLibs) -> Unit) {
    val libs = the<LibrariesForLibs>()
    block.invoke(libs)
}

val Project.projectJavaVersion: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.javaVersion.get().toInt())