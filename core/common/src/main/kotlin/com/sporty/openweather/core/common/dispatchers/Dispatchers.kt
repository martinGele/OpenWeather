package com.sporty.openweather.core.common.dispatchers

import javax.inject.Qualifier

/** Qualifies the IO [kotlinx.coroutines.CoroutineDispatcher] for injection. */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

/** Qualifies the Default (CPU) [kotlinx.coroutines.CoroutineDispatcher]. */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher
