package ru.binnyatoff.githubclient.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.toUser
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
            HomeFragmentEvent.HomeInit -> getRepository()
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Loaded) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> getRepository(true)
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Error) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> getRepository(true)
        }
    }

    private fun reduce(event: HomeFragmentEvent, currentState: HomeFragmentState.Empty) {
        when (event) {
            HomeFragmentEvent.ReloadScreen -> getRepository(true)
        }
    }

    private fun getRepository(needsToRefresh: Boolean = false) {
        if (needsToRefresh) {
            _homeViewState.postValue(HomeFragmentState.Loading)
        }
        viewModelScope.launch {
            try {
                val users = repository.getUsers()

                if (users.isSuccessful) {
                    val body = users.body()
                    if (body != null) {
                        val usersList = body.map { userNetwork ->
                            userNetwork.toUser()
                        }
                        _homeViewState.postValue(HomeFragmentState.Loaded(usersList))

                    } else
                        _homeViewState.postValue(HomeFragmentState.Empty)
                }
            } catch (e: Exception) {
                _homeViewState.postValue(HomeFragmentState.Error("$e"))
            }
        }
    }
}


