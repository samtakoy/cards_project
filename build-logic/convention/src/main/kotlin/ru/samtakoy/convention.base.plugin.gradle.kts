
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.samtakoy.ext.projectJavaVersion
import ru.samtakoy.ext.withVersionCatalog


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

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
            }
        }
    }
}