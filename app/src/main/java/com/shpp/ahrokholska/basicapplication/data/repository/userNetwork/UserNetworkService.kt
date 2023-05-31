package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserNetworkService {
    @POST("login")
    suspend fun authorizeUser(@Body user: UserCredentials): ResponseBody

    @POST("refresh")
    suspend fun refreshToken(@Header("RefreshToken") token: String): ResponseBody

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") id: Long, @Header("Authorization") tokenHeader: String
    ): ResponseBody

    @FormUrlEncoded
    @POST("users")
    suspend fun createUser(
        @Field("email") email: String, @Field("password") password: String,
        @Field("name") name: String?, @Field("phone") phone: String?
    ): ResponseBody
}