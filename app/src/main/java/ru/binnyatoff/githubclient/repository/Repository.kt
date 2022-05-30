package ru.binnyatoff.githubclient.repository

import retrofit2.Response
import ru.binnyatoff.githubclient.repository.models.Search

import ru.binnyatoff.githubclient.data.network.retrofit.Api
import ru.binnyatoff.githubclient.data.database.Dao
import ru.binnyatoff.githubclient.data.network.models.UserDetailsNetwork
import ru.binnyatoff.githubclient.data.network.models.UserNetwork

class Repository(private var api: Api, private var dao: Dao) {

    suspend fun getUsers(): Response<List<UserNetwork>> {
       return api.get_users()
    }

    suspend fun getFollowers(user: String): Response<List<UserNetwork>>{
        return api.get_followers(user)
    }

    suspend fun getUserDetails(user: String): Response<UserDetailsNetwork>{
        return api.get_user_details(user)
    }

    suspend fun search(query:String): Response<Search>{
        return api.search(query)
    }

}