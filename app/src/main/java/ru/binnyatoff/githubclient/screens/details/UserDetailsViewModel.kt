package ru.binnyatoff.githubclient.screens.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.toUserDetails
import ru.binnyatoff.githubclient.screens.helpers.EventHandler
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    EventHandler<UserDetailsEvent> {

    private val _userDetailsState = MutableLiveData<UserDetailsState>(UserDetailsState.Loading)
    val userDetailsState: LiveData<UserDetailsState> = _userDetailsState


    override fun obtainEvent(event: UserDetailsEvent) {
        when (val currentState = _userDetailsState.value) {
            is UserDetailsState.Loading -> reduce(event, currentState)
            is UserDetailsState.LoadedWithName -> reduce(event, currentState)
            is UserDetailsState.LoadedWithoutName -> reduce(event, currentState)
            is UserDetailsState.Empty -> reduce(event, currentState)
            is UserDetailsState.Error -> reduce(event, currentState)
        }
    }

    private fun reduce(event: UserDetailsEvent, state: UserDetailsState.Loading) {
        when (event) {
           is UserDetailsEvent.ScreenInit -> getUserDetails(event.user)
        }
    }

    private fun reduce(event: UserDetailsEvent, state: UserDetailsState.LoadedWithName) {
        when (event) {
            is UserDetailsEvent.ScreenInit -> getUserDetails(event.user)
        }
    }

    private fun reduce(event: UserDetailsEvent, state: UserDetailsState.LoadedWithoutName) {
        when (event) {
            is UserDetailsEvent.ScreenInit -> getUserDetails(event.user)
        }
    }

    private fun reduce(event: UserDetailsEvent, state: UserDetailsState.Empty) {
        when (event) {
            is UserDetailsEvent.ScreenInit -> getUserDetails(event.user)
        }
    }

    private fun reduce(event: UserDetailsEvent, state: UserDetailsState.Error) {
        when (event) {
            is UserDetailsEvent.ScreenInit -> getUserDetails(event.user)
        }
    }

    private fun getUserDetails(user: String?) {
        if (user != null) {
            viewModelScope.launch {
                try {
                    val response = repository.getUserDetails(user)
                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.e("TAG", body.toString())
                        if (body != null) {
                            Log.e("TAG", "${body}")
                            val userDetails = body.toUserDetails()
                            if (userDetails.name != null) {
                                _userDetailsState.postValue(
                                    UserDetailsState.LoadedWithName(
                                        userDetails = userDetails
                                    )
                                )
                                Log.e("TAG", "${userDetails}")
                            } else {
                                Log.e("TAG", "Null -> ${userDetails.name}")
                                _userDetailsState.postValue(
                                    UserDetailsState.LoadedWithoutName(
                                        userDetails = userDetails
                                    )
                                )
                            }

                        } else {
                            Log.e("TAG", "$body")
                            _userDetailsState.postValue(UserDetailsState.Empty)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "$e")
                    _userDetailsState.postValue(UserDetailsState.Error(e.toString()))
                }
            }
        }
    }
}