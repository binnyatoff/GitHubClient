package ru.binnyatoff.githubclient.data.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.binnyatoff.githubclient.repository.models.SearchResult
import ru.binnyatoff.githubclient.data.network.models.UserDetailsNetwork
import ru.binnyatoff.githubclient.data.network.models.UserNetwork

interface Api {
    @GET("./users")
    suspend fun get_users(): Response<List<UserNetwork>>

    @GET("/users/{user}/followers")
    suspend fun get_followers(@Path("user") user: String): Response<List<UserNetwork>>

    @GET("/users/{user}")
    suspend fun get_user_details(@Path("user") user: String): Response<UserDetailsNetwork>

    @GET("/search/users")
    suspend fun search(@Query("q")query: String): Response<SearchResult>
}