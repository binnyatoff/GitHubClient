package ru.binnyatoff.githubclient.screens.feed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.models.Search
import ru.binnyatoff.githubclient.retrofit.Api
import ru.binnyatoff.githubclient.models.User
import javax.inject.Inject

interface refresh{
    fun refreshUsers()
}

@HiltViewModel
class UsersViewModel @Inject constructor(private val api: Api) : ViewModel(),refresh {

    val errorMessage = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var userList = MutableLiveData<List<User>>()
    var testuserList = MutableLiveData<Search>()

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
                }
            } catch (e: Exception) {
                loading.postValue(false)
                errorMessage.postValue(e.toString())
                }
        }
    }

    fun search(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.e("TAG", "Loading")
                val response = api.search(query)
                if (response.isSuccessful){
                    testuserList.postValue(response.body())
                    Log.e("TAG", "${response.body()}")
                }
            } catch (e: Exception){
                Log.e("TAG", "Error $e")
            }
        }
    }
    override fun refreshUsers() {
        getAllUsers()
    }
}


