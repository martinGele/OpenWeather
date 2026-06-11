import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.sporty.openweather.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Applies and configures Jetpack Compose. Apply on top of either
 * `openweather.android.application` or `openweather.android.library`.
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        // Works whether the host module is an application or a library.
        extensions.findByType(ApplicationExtension::class.java)?.let { configureAndroidCompose(it) }
        extensions.findByType(LibraryExtension::class.java)?.let { configureAndroidCompose(it) }
        Unit
    }
}
