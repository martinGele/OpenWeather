package com.sporty.openweather.core.network.di

import com.sporty.openweather.core.network.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Generic HTTP infrastructure shared by the whole app. Provides a single
 * [OkHttpClient] and [Retrofit]; feature modules create only their API interface
 * from the shared [Retrofit]. Cross-cutting interceptors (auth, headers) are
 * contributed via the [Set] multibinding, so this module stays feature-agnostic.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
        )
        interceptors.forEach(builder::addInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        @BaseUrl baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

/** Declares the interceptor set so it may be empty when no module contributes one. */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkInterceptorsModule {
    @Multibinds
    abstract fun interceptors(): Set<Interceptor>
}
