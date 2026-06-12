plugins {
    alias(libs.plugins.openweather.android.feature)
}

android {
    namespace = "com.sporty.openweather.feature.search.presentation"
}

dependencies {
    api(project(":feature:search:domain"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
}
