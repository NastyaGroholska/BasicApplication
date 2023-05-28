package com.shpp.ahrokholska.basicapplication.domain.repository.userRepository

import com.shpp.ahrokholska.basicapplication.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}