package com.shpp.ahrokholska.basicapplication.data.repositories.contacts

import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactsNetworkService {
    @GET("users/{userId}/contacts")
    suspend fun getUserContacts(
        @Path("userId") userId: Long, @Header("Authorization") tokenHeader: String
    ): ResponseBody

    @FormUrlEncoded
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Path("userId") userId: Long, @Header("Authorization") tokenHeader: String,
        @Field("contactId") contactId: Long
    ): ResponseBody

    @GET("users")
    suspend fun getAllUsers(@Header("Authorization") tokenHeader: String): ResponseBody

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Path("userId") userId: Long, @Path("contactId") contactId: Long,
        @Header("Authorization") tokenHeader: String,
    ): ResponseBody
}