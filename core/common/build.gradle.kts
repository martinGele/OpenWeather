plugins {
    alias(libs.plugins.openweather.android.library)
    alias(libs.plugins.openweather.android.hilt)
}

android {
    namespace = "com.sporty.openweather.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
