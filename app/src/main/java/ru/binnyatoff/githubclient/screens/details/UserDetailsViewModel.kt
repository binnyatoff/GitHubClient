package ru.binnyatoff.githubclient.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.models.UserDetails
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    val errorMessage = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val userDetails = MutableLiveData<UserDetails>()
    var job: Job? = null

    fun getUserDetails(user: String) {
        loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getUserDetails(user)
                if (response.isSuccessful) {
                    userDetails.run { postValue(response.body()) }
                    loading.postValue(false)
                }
            } catch (e: Exception) {
                loading.postValue(false)
                errorMessage.postValue(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}