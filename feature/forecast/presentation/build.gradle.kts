plugins {
    alias(libs.plugins.openweather.android.feature)
}

android {
    namespace = "com.sporty.openweather.feature.forecast.presentation"
}

dependencies {
    implementation(project(":feature:forecast:domain"))
}
