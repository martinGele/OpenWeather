plugins {
    alias(libs.plugins.openweather.android.library)
    alias(libs.plugins.openweather.android.hilt)
}

android {
    namespace = "com.sporty.openweather.feature.search.data"
}

dependencies {
    implementation(project(":feature:search:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit.core)
    implementation(libs.gson)
}
