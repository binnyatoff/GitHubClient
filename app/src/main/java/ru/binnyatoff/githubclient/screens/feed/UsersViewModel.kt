package ru.binnyatoff.githubclient.screens.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.models.Search
import ru.binnyatoff.githubclient.retrofit.Api
import ru.binnyatoff.githubclient.models.User
import ru.binnyatoff.githubclient.room.Dao
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val api: Api, private val dao: Dao) :
    ViewModel() {
    val errorMessage = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var userList = MutableLiveData<List<User>>()
    var searchList = MutableLiveData<Search>()

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.listUser()
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                    loading.postValue(false)
                    errorMessage.postValue(false)
                    response.body()?.let {
                        addToDatabase(it)
                    }
                }
            } catch (e: Exception) {
                loading.postValue(false)
                errorMessage.postValue(true)
                getInDatabase()
            }
        }
    }

    fun search(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.search(query)
                if (response.isSuccessful) {
                    searchList.postValue(response.body())
                }
            } catch (e: Exception) {
            }
        }
    }

    fun refreshUsers() {
        getAllUsers()
    }

    private fun addToDatabase(user: List<User>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addUser(user)
        }
    }

    private fun getInDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            userList.postValue(dao.readAllData())
        }
    }
}


