package ru.binnyatoff.githubclient.screens.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.toUserDetails
import ru.binnyatoff.githubclient.screens.home.EventHandler
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    EventHandler<UserDetailsState> {

    private val _userDetailsState = MutableLiveData<UserDetailsState>()
    val userDetailsState: LiveData<UserDetailsState> = _userDetailsState

    fun getUserDetails(user: String) {
        _userDetailsState.postValue(UserDetailsState.Loading)
        viewModelScope.launch {
            try {
                val response = repository.getUserDetails(user)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val userDetails = body.toUserDetails()
                        if (body.name != null) {
                            _userDetailsState.postValue(UserDetailsState.LoadedWithName(userDetails = userDetails))
                        } else {
                            _userDetailsState.postValue(
                                UserDetailsState.LoadedWithoutName(
                                    userDetails = userDetails
                                )
                            )
                        }
                    } else {
                        _userDetailsState.postValue(UserDetailsState.Empty)
                    }
                }
            } catch (e: Exception) {
                _userDetailsState.postValue(UserDetailsState.Error(e.toString()))
            }
        }
    }

    override fun obtainEvent(event: UserDetailsState) {
        //TODO
    }
}