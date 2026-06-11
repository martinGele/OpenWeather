plugins {
    alias(libs.plugins.openweather.android.library)
    alias(libs.plugins.openweather.android.hilt)
}

android {
    namespace = "com.sporty.openweather.feature.forecast.data"
}

dependencies {
    implementation(project(":feature:forecast:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit.core)   // Retrofit type + retrofit.create() for the API
    implementation(libs.gson)            // @SerializedName on the DTOs
}
