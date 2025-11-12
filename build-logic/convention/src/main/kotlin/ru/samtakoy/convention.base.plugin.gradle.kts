
import com.android.build.gradle.BaseExtension
import ru.samtakoy.ext.projectJavaVersion
import ru.samtakoy.ext.withVersionCatalog

// android {}
configure<BaseExtension> {
    project.withVersionCatalog { libs ->
        compileSdkVersion(libs.versions.compileSdk.get().toInt())
        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()

            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = projectJavaVersion
            targetCompatibility = projectJavaVersion
        }
    }
}
