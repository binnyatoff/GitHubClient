package ru.binnyatoff.githubclient.screens.followers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.models.User
import ru.binnyatoff.githubclient.repository.toUser
import javax.inject.Inject


@HiltViewModel
class FollowersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val followersList = MutableLiveData<List<User>>()
    var job: Job? = null

    fun getFollowers(user: String) {
        loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getFollowers(user)
                if (response.isSuccessful) {
                    val body = response.body()
                    followersList.postValue(response.body()?.map{
                        it.toUser()
                    })
                    loading.postValue(false)
                }
            } catch (e: Exception) {
                loading.postValue(false)
                errorMessage.postValue(e.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}


