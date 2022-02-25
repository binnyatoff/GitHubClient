package ru.binnyatoff.githubclient.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.data.Repository
import ru.binnyatoff.githubclient.data.models.User
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val userList = MutableLiveData<List<User>>()
    var job: Job? = null

    init {
        getRepository()
    }

    private fun getRepository() {
        job = CoroutineScope(Dispatchers.IO).launch {
            userList.postValue(repository.getUsers())
            loading.postValue(false)
        }
    }

    fun refreshUsers() {
        getRepository()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}


