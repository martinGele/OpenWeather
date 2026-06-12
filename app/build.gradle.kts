import java.util.Properties

plugins {
    alias(libs.plugins.openweather.android.application)
    alias(libs.plugins.openweather.android.compose)
    alias(libs.plugins.openweather.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

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
    implementation(project(":core:ui"))
    implementation(project(":feature:forecast:presentation"))
    implementation(project(":feature:search:presentation"))

    implementation(project(":feature:forecast:data"))
    implementation(project(":feature:search:data"))
    implementation(project(":core:network"))

    implementation(libs.okhttp.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.compose)
}

