plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.module.core.db.model.card)
            implementation(projects.module.core.db.model.learncourse)
            implementation(projects.module.core.db.model.qpack)
            implementation(projects.module.core.db.model.theme)
            implementation(projects.module.core.db.model.view)
        }
    }
}

android {
    namespace = "ru.samtakoy.core.db.api"
}