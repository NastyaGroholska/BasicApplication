package com.shpp.ahrokholska.basicapplication.domain.di

import com.shpp.ahrokholska.basicapplication.data.repository.UserLocalRepositoryImpl
import com.shpp.ahrokholska.basicapplication.data.repository.userNetwork.UserNetworkRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserLocalRepositoryImpl): UserLocalRepository

    @Binds
    @Singleton
    abstract fun bindUserNetworkRepository(userNetworkRepositoryImpl: UserNetworkRepositoryImpl): UserNetworkRepository
}