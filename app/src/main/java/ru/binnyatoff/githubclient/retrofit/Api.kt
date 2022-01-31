package ru.binnyatoff.githubclient.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("./users")
    suspend fun listUser(): Response<List<User>>
    @GET("/users/{user}/followers")
    suspend fun followers(@Path("user") user: String): Response<List<User>>
}