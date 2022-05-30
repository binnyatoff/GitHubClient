package ru.binnyatoff.githubclient.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _state = MutableLiveData<HomeFragmentState>()
    private var job: Job? = null

    val state: LiveData<HomeFragmentState> = _state

    private fun getRepository() {
        _state.postValue(HomeFragmentState.Loading)
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val users = repository.getUsers()
                if (users.isSuccessful) {
                    val body = users.body()
                    if (body != null) {
                        _state.postValue(HomeFragmentState.Loaded(body))
                    } else
                        _state.postValue(HomeFragmentState.Empty)
                }
            } catch (e: Exception) {
                _state.postValue(HomeFragmentState.Error("$e"))
            }
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


