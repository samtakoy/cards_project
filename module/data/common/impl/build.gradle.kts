plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.room)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // room
            // ksp(libs.androidx.room.compiler)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // koin
            implementation(libs.koin.core)

            implementation(projects.module.common.utils)
            implementation(projects.module.platform.api)
            implementation(projects.module.data.common.api)

            implementation(projects.module.domain.card.model)
            implementation(projects.module.domain.learncourse.model)
            implementation(projects.module.domain.qpack.model)
            implementation(projects.module.domain.theme.model)
            implementation(projects.module.domain.view.model)
        }
    }
}

android {
    namespace = "ru.samtakoy.data.impl"
}

room {
    schemaDirectory("$projectDir/schemas")
}

// Настройка KSP для всех таргетов
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    // add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    // add("kspIosX64", libs.androidx.room.compiler)
    // add("kspIosArm64", libs.androidx.room.compiler)
}