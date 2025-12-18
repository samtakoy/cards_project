plugins {
    id("convention.kmp-lib.plugin")
    alias(libs.plugins.room)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // koin
            implementation(libs.koin.core)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.db.repoApi)

            implementation(projects.module.core.db.model.card)
            implementation(projects.module.core.db.model.learncourse)
            implementation(projects.module.core.db.model.qpack)
            implementation(projects.module.core.db.model.theme)
            implementation(projects.module.core.db.model.view)
        }
    }
}

android {
    namespace = "ru.samtakoy.core.db.impl"
}

room {
    schemaDirectory("$projectDir/schemas")
}

// Настройка KSP для всех таргетов
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}