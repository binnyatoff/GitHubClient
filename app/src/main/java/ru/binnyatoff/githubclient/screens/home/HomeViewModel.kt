package ru.binnyatoff.githubclient.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.binnyatoff.githubclient.data.network.NetworkResult
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.toUser
import ru.binnyatoff.githubclient.screens.helpers.EventHandler
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) :
    ViewModel(), EventHandler<HomeFragmentEvent> {

    private val _homeViewState = MutableLiveData<HomeFragmentState>(HomeFragmentState.Loading)
    val homeViewState: LiveData<HomeFragmentState> = _homeViewState

    override fun obtainEvent(event: HomeFragmentEvent) {
        when (val currentState = _homeViewState.value) {
            is HomeFragmentState.Loading -> reduce(event, currentState)
            is HomeFragmentState.Loaded -> reduce(event, currentState)
            is HomeFragmentState.Error -> reduce(event, currentState)
            is HomeFragmentState.Empty -> reduce(event, currentState)
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Loading) {
        when (event) {
            HomeFragmentEvent.HomeInit -> {
                getRepository()
            }
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Loaded) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> {
                _homeViewState.postValue(HomeFragmentState.Loading)
                getRepository()
            }
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Error) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> {
                _homeViewState.postValue(HomeFragmentState.Loading)
                getRepository()
            }
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Empty) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> {
                _homeViewState.postValue(HomeFragmentState.Loading)
                getRepository()
            }
        }
    }

    private fun getRepository() {
        Log.e("ViewModel", "Loading")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val response = repository.getUsersNew()) {
                    is NetworkResult.Error -> {}
                    is NetworkResult.Exception -> {}
                    is NetworkResult.Success -> {
                        val userList = response.data.map { userNetwork ->
                            userNetwork.toUser()
                        }
                        _homeViewState.postValue(
                            HomeFragmentState.Loaded(
                                userList = userList
                            )
                        )
                    }
                }
            }
        }
    }
}


