package com.sporty.openweather.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

private val JAVA_VERSION = JavaVersion.VERSION_11
private val JVM_TARGET = JvmTarget.JVM_11

/**
 * Configure base Kotlin with Android options. Shared by the application and
 * library convention plugins so SDK levels and compiler flags live in one place.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    // AGP 9 removed the lambda-DSL overloads from the base CommonExtension
    // interface, so configure via the property getters instead.
    commonExtension.apply {
        compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
        defaultConfig.minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        compileOptions.sourceCompatibility = JAVA_VERSION
        compileOptions.targetCompatibility = JAVA_VERSION
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

/** Configure base Kotlin options for a pure JVM (non-Android) module. */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

/** Shared Kotlin compiler options for both Android and JVM modules. */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    val compilerOptions = when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> error("Unsupported project extension $this ${T::class}")
    }
    compilerOptions.apply {
        jvmTarget.set(JVM_TARGET)
        allWarningsAsErrors.set(false)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}
