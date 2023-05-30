package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserNetworkService {
    @POST("login")
    suspend fun authorizeUser(@Body user: UserCred): ResponseBody

    @POST("refresh")
    suspend fun refreshToken(@Header("RefreshToken") token: String): ResponseBody

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") id: Long, @Header("Authorization") tokenHeader: String
    ): ResponseBody
}