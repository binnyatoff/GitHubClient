package ru.binnyatoff.githubclient.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.binnyatoff.githubclient.data.models.User
import ru.binnyatoff.githubclient.data.models.User_Details
import ru.binnyatoff.githubclient.data.models.Search

interface Api {
    @GET("./users")
    suspend fun get_users(): Response<List<User>>

    @GET("/users/{user}/followers")
    suspend fun get_followers(@Path("user") user: String): Response<List<User>>

    @GET("/users/{user}")
    suspend fun get_user_details(@Path("user") user: String): Response<User_Details>

    @GET("/search/users")
    suspend fun search(@Query("q")query: String): Response<Search>
}