package com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository

import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ContactsRepositoryModule {
    @Provides
    fun provideContactsRepository(): ContactsRepository {
        return HardcodedContactsRepositoryImpl()
    }
}