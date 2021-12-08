package ru.binnyatoff.githubclient.screens.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.retrofit.Api
import ru.binnyatoff.githubclient.retrofit.User
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val api: Api) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var userList = MutableLiveData<List<User>>()

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
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
}


