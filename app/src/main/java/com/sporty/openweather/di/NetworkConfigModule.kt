package com.sporty.openweather.di

import com.sporty.openweather.BuildConfig
import com.sporty.openweather.core.network.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

/**
 * Backend configuration owned by the composition root: which host the app talks to
 * and how requests are authenticated. Contributed into the shared :core:network
 * client/Retrofit, so the network module stays feature-agnostic.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @IntoSet
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", BuildConfig.OPENWEATHER_API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }
}
