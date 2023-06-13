package com.shpp.ahrokholska.basicapplication.domain.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitSimple

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitWithInterceptor