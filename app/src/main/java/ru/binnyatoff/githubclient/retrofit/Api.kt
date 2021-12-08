package ru.binnyatoff.githubclient.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("./users")
    suspend fun listUser(): Response<List<User>>
}