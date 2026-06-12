plugins {
    alias(libs.plugins.openweather.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sporty.openweather.feature.forecast.presentation"
}

dependencies {
    implementation(project(":feature:forecast:domain"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
}
