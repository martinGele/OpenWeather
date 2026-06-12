plugins {
    alias(libs.plugins.openweather.android.feature)
}

android {
    namespace = "com.sporty.openweather.feature.search.presentation"
}

dependencies {
    implementation(project(":feature:search:domain"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
}
