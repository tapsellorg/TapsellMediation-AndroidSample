import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.util.Properties

class TapsellAppPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("com.android.application")

        android {
            compileSdk = 35
            defaultConfig {
                minSdk = 21
                targetSdk = 35
            }
        }

        configureAppSigning()
    }
}

private fun Project.android(block: ApplicationExtension.() -> Unit) {
    extensions.configure<ApplicationExtension>(block)
}

private fun Project.configureAppSigning() {
    val rootFile = rootDir / ".credential"
    val propsFile = rootFile / "dev-keystore.properties"
    check(propsFile.exists()) { "no credential found in $propsFile" }
    val props = Properties().apply { load(propsFile.reader()) }

    android {
        signingConfigs {
            create("dev-key") {
                storeFile = rootFile / props["DEV_KEY_FILE"].toString()
                storePassword = props["DEV_KEY_FILE_PASSWORD"].toString()
                keyAlias = props["DEV_KEY_ALIAS"].toString()
                keyPassword = props["DEV_KEY_PASSWORD"].toString()
            }
        }

        // applied to all build-types:
        // all applications are public and no need to separate debug and release types
        buildTypes.configureEach {
            signingConfig = signingConfigs.getByName("dev-key")
        }
    }
}

private operator fun File.div(next: String): File = File("${this.path}/$next")
