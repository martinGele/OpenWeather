import com.android.build.api.dsl.ApplicationExtension
import com.sporty.openweather.buildlogic.configureKotlinAndroid
import com.sporty.openweather.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            defaultConfig {
                targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                versionCode = 1
                versionName = "1.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}
