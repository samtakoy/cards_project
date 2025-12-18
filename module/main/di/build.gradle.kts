plugins {
    id("convention.kmp-lib.plugin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(projects.module.common.utils.common)
            implementation(projects.module.core.log)

            implementation(projects.module.core.db.repoImpl)
            implementation(projects.module.core.network)
            implementation(projects.module.utilFeatures.task.impl)

            implementation(projects.module.features.card.impl)
            implementation(projects.module.features.favorites.impl)
            implementation(projects.module.features.learncourse.impl)
            implementation(projects.module.features.qpack.impl)
            implementation(projects.module.features.theme.impl)
            implementation(projects.module.features.view.impl)

            // features
            implementation(projects.module.features.import.impl)
            implementation(projects.module.features.speech.impl)
            implementation(projects.module.features.speech.presentation)
            implementation(projects.module.utilFeatures.download.impl)

            implementation(projects.module.main.screen)
            implementation(projects.module.features.card.presentationImpl)
            implementation(projects.module.features.qpack.presentationImpl)
            implementation(projects.module.features.theme.presentationImpl)
            implementation(projects.module.features.learncourse.presentationImpl)
            implementation(projects.module.features.favorites.presentationImpl)
            implementation(projects.module.features.settings.presentationImpl)
            implementation(projects.module.features.view.presentationImpl)

            implementation(projects.module.utilFeatures.notification.impl)
            implementation(projects.module.utilFeatures.permissions.impl)
        }
    }
}

android {
    namespace = "com.example.maindi"
}
