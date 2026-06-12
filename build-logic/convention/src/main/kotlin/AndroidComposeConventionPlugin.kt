import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.sporty.openweather.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.findByType(ApplicationExtension::class.java)?.let { configureAndroidCompose(it) }
        extensions.findByType(LibraryExtension::class.java)?.let { configureAndroidCompose(it) }
        Unit
    }
}
