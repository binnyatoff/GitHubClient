package ru.binnyatoff.githubclient.screens.followers

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
class FollowersViewModel @Inject constructor(private val api: Api) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var userList = MutableLiveData<List<User>>()

    fun getFollowers(user: String) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.followers(user)
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


