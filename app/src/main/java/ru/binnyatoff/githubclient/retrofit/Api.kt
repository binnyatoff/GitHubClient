package ru.binnyatoff.githubclient.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.binnyatoff.githubclient.models.User
import ru.binnyatoff.githubclient.models.User_Details

interface Api {
    @GET("./users")
    suspend fun listUser(): Response<List<User>>

    @GET("/users/{user}/followers")
    suspend fun followers(@Path("user") user: String): Response<List<User>>

    //https://api.github.com/users/{user}
    @GET("/users/{user}")
    suspend fun user_details(@Path("user") user: String): Response<User_Details>
}