package com.sporty.openweather.core.network

import javax.inject.Qualifier

/** Qualifies the backend base URL string, supplied by the composition root. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl
