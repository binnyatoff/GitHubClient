package ru.binnyatoff.githubclient.repository

import retrofit2.Response
import ru.binnyatoff.githubclient.repository.models.SearchResult
import ru.binnyatoff.githubclient.data.network.retrofit.Api
import ru.binnyatoff.githubclient.data.database.Dao
import ru.binnyatoff.githubclient.data.network.NetworkResult
import ru.binnyatoff.githubclient.data.network.handleApi
import ru.binnyatoff.githubclient.data.network.models.UserDetailsNetwork
import ru.binnyatoff.githubclient.data.network.models.UserNetwork

class Repository(private var api: Api, private var dao: Dao) {

    suspend fun getFollowers(user: String): Response<List<UserNetwork>> {
        return api.get_followers(user)
    }

    suspend fun getUserDetails(user: String): Response<UserDetailsNetwork> {
        return api.get_user_details(user)
    }

    suspend fun search(query: String): Response<SearchResult> {
        return api.search(query)
    }

    suspend fun getUsersNew(): NetworkResult<List<UserNetwork>> {
        return handleApi { api.get_users() }
    }

    suspend fun getFollowersNew(user: String): NetworkResult<List<UserNetwork>> {
        return handleApi {
            api.get_followers(user)
        }
    }

    suspend fun getUserDetailsNew(user: String): NetworkResult<UserDetailsNetwork> {
        return handleApi {
            api.get_user_details(user)
        }
    }

    suspend fun searchNew(query: String): NetworkResult<SearchResult> {
        return handleApi {
            api.search(query)
        }
    }
}