package ru.binnyatoff.githubclient.data

import android.util.Log
import retrofit2.Response
import ru.binnyatoff.githubclient.data.models.Search
import ru.binnyatoff.githubclient.data.models.User
import ru.binnyatoff.githubclient.data.models.User_Details
import ru.binnyatoff.githubclient.data.retrofit.Api
import ru.binnyatoff.githubclient.data.room.Dao

class Repository(private var api: Api, private var dao: Dao) {

    suspend fun getUsers(): List<User> {
        val readAllData = dao.readAllData()
        try {
            val get_users = this.api.get_users()
            if (get_users.isSuccessful) {
                if (get_users.body() == readAllData) {
                    Log.e("TAG", "Loaded from api")
                    return get_users.body()!!
                }
                else {
                    get_users.body()?.let {
                        this.dao.delete()
                        this.dao.addUser(it)
                    }
                    Log.e("TAG", "Loaded from api and added to database")
                    return get_users.body() ?: emptyList()
                }
            } else {
                Log.e("TAG", "Loaded from database")
                return readAllData
            }
        } catch (e: Exception) {
            Log.e("TAG", "No internet connection loaded from database")
            return readAllData
        }
    }

    suspend fun getFollowers(user: String): Response<List<User>>{
        return api.get_followers(user)
    }

    suspend fun getUserDetails(user: String): Response<User_Details>{
        return api.get_user_details(user)
    }

    suspend fun search(query:String): Response<Search>{
        return api.search(query)
    }

}