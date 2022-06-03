package ru.binnyatoff.githubclient.screens.home

import ru.binnyatoff.githubclient.repository.models.User

sealed class HomeFragmentState {
    data class Loaded(val userList: List<User>) : HomeFragmentState()
    data class Error(val errorMessage: String) : HomeFragmentState()
    object Loading : HomeFragmentState()
    object Empty : HomeFragmentState()
}

sealed class HomeFragmentEvent {
    object HomeInit : HomeFragmentEvent()
    object ReloadScreen : HomeFragmentEvent()
    object onUserClick : HomeFragmentEvent()
}

