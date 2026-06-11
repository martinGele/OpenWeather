import com.sporty.openweather.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Convention for feature *presentation* modules: an Android library with Compose +
 * Hilt and the shared cross-cutting dependencies (`:core:ui`, `:core:common`).
 * Each presentation module declares its own `:feature:x:domain` — there is no
 * shared `:core:domain`, since features own their vertical slice.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("openweather.android.library")
            apply("openweather.android.compose")
            apply("openweather.android.hilt")
        }

        dependencies {
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:common"))

            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
            add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
        }
    }
}
