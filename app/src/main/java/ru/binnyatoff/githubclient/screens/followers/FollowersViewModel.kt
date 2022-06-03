package ru.binnyatoff.githubclient.screens.followers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.toUser
import ru.binnyatoff.githubclient.screens.helpers.EventHandler

import javax.inject.Inject


@HiltViewModel
class FollowersViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    EventHandler<FollowersFragmentEvent> {

    private val _followerViewState =
        MutableLiveData<FollowersFragmentState>(FollowersFragmentState.Loading)

    val followersViewState: LiveData<FollowersFragmentState> = _followerViewState

    override fun obtainEvent(event: FollowersFragmentEvent) {
        when (val currentState = _followerViewState.value) {
            is FollowersFragmentState.Loading -> reduce(event, currentState)
            is FollowersFragmentState.Loaded -> reduce(event, currentState)
            is FollowersFragmentState.Empty -> reduce(event, currentState)
            is FollowersFragmentState.Error -> reduce(event, currentState)
        }
    }

    private fun reduce(event: FollowersFragmentEvent, state: FollowersFragmentState.Loading) {
        when (event) {
            is FollowersFragmentEvent.HomeInit -> getFollowers(event.user)
        }
    }

    private fun reduce(event: FollowersFragmentEvent, state: FollowersFragmentState.Loaded) {
        when (event) {
            is FollowersFragmentEvent.HomeInit -> getFollowers(event.user)
        }
    }

    private fun reduce(event: FollowersFragmentEvent, state: FollowersFragmentState.Empty) {
        when (event) {
            is FollowersFragmentEvent.HomeInit -> getFollowers(event.user)
        }
    }

    private fun reduce(event: FollowersFragmentEvent, state: FollowersFragmentState.Error) {
        when (event) {
            is FollowersFragmentEvent.HomeInit -> getFollowers(event.user)
        }
    }


    private fun getFollowers(user: String) {
        _followerViewState.postValue(FollowersFragmentState.Loading)
        viewModelScope.launch {
            try {
                val response = repository.getFollowers(user)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val userList = body.map {
                            it.toUser()
                        }
                        _followerViewState.postValue(FollowersFragmentState.Loaded(userList))
                    } else {
                        _followerViewState.postValue(FollowersFragmentState.Empty)
                    }
                }
            } catch (e: Exception) {
                _followerViewState.postValue(FollowersFragmentState.Error(e.toString()))
            }
        }
    }
}


