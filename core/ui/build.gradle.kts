plugins {
    alias(libs.plugins.openweather.android.library)
    alias(libs.plugins.openweather.android.compose)
}

android {
    namespace = "com.sporty.openweather.core.ui"
}

// Pure design system: theming, spacing, radii, fonts. No domain/data knowledge —
// nothing here should ever import a model, use case or repository.
dependencies {
}
