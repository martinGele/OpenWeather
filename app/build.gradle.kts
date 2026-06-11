import java.util.Properties

plugins {
    alias(libs.plugins.openweather.android.application)
    alias(libs.plugins.openweather.android.compose)
    alias(libs.plugins.openweather.android.hilt)
}

// Secrets (e.g. API keys) live in local.properties — gitignored, never committed.
// CI provides them via -P / env; missing key falls back to empty.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
val openWeatherApiKey: String =
    (localProperties.getProperty("OPENWEATHER_API_KEY")
        ?: providers.gradleProperty("OPENWEATHER_API_KEY").orNull
        ?: "")

android {
    namespace = "com.sporty.openweather"

    defaultConfig {
        applicationId = "com.sporty.openweather"
        buildConfigField("String", "BASE_URL", "\"https://api.openweathermap.org/\"")
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"$openWeatherApiKey\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    // Directly referenced: theme (:core:ui) and the screen entry point.
    implementation(project(":core:ui"))
    implementation(project(":feature:forecast:presentation"))

    // Not referenced in app code, but required on the compile classpath so Hilt
    // aggregates its @InstallIn modules into the app graph (WeatherRepositoryImpl,
    // the weather Retrofit config, and the network/dispatcher providers it pulls
    // in transitively from :core:network and :core:common).
    implementation(project(":feature:forecast:data"))

    // Backend config (base URL + auth interceptor) provided into :core:network.
    implementation(project(":core:network"))
    implementation(libs.okhttp.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
