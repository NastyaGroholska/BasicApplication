package com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository

import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ContactsRepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideContactsRepository(): ContactsRepository {
        return HardcodedContactsRepositoryImpl()
    }
}