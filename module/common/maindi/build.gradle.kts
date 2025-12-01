plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(projects.module.common.utils)

            implementation(projects.module.data.common.impl)
            implementation(projects.module.data.task.impl)
            implementation(projects.module.features.speech.data)

            implementation(projects.module.domain.card.impl)
            implementation(projects.module.domain.favorites.impl)
            implementation(projects.module.domain.learncourse.impl)
            implementation(projects.module.domain.qpack.impl)
            implementation(projects.module.domain.theme.impl)
            implementation(projects.module.domain.view.impl)
            implementation(projects.module.features.import.impl)
            implementation(projects.module.features.speech.domain.impl)

            implementation(projects.module.presentation.main.impl)
            implementation(projects.module.presentation.cards.impl)
            implementation(projects.module.presentation.qpacks.impl)
            implementation(projects.module.presentation.themes.impl)
            implementation(projects.module.presentation.courses.impl)
            implementation(projects.module.presentation.favorites.impl)
            implementation(projects.module.presentation.settings.impl)
            implementation(projects.module.presentation.viewshistory.impl)

            implementation(projects.module.platform.notification.impl)
            implementation(projects.module.platform.permissions.impl)
            implementation(projects.module.features.speech.platform)
        }
    }
}

android {
    namespace = "com.example.maindi"
}
