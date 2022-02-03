package ru.binnyatoff.githubclient.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.binnyatoff.githubclient.models.User
import ru.binnyatoff.githubclient.models.User_Details
import ru.binnyatoff.githubclient.models.Search

interface Api {
    @GET("./users")
    suspend fun listUser(): Response<List<User>>

    @GET("/users/{user}/followers")
    suspend fun followers(@Path("user") user: String): Response<List<User>>

    @GET("/users/{user}")
    suspend fun user_details(@Path("user") user: String): Response<User_Details>

    @GET("/search/users")
    suspend fun search(@Query("q")query: String): Response<Search>
}