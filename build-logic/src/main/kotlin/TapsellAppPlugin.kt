import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

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
    }
}

private fun Project.android(block: ApplicationExtension.() -> Unit) {
    extensions.configure<ApplicationExtension>(block)
}