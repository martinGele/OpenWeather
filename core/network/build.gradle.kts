plugins {
    alias(libs.plugins.openweather.android.library)
    alias(libs.plugins.openweather.android.hilt)
}

android {
    namespace = "com.sporty.openweather.core.network"
}

dependencies {
    implementation(libs.retrofit.core)          // Retrofit + HttpException in safeApiCall
    implementation(libs.retrofit.gson)           // shared Gson converter
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
}
