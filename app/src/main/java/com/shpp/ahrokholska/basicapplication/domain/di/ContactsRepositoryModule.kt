package com.shpp.ahrokholska.basicapplication.domain.di

import com.shpp.ahrokholska.basicapplication.data.repositories.contacts.ContactsNetworkRepository
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindContactsRepository(impl: ContactsNetworkRepository): ContactsRepository
}