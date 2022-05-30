package ru.binnyatoff.githubclient.screens.home

import ru.binnyatoff.githubclient.repository.models.User

sealed class HomeFragmentState {
    class Loaded(val userList: List<User>) : HomeFragmentState()
    class Error(val errorMessage: String) : HomeFragmentState()
    object Loading : HomeFragmentState()
    object Empty : HomeFragmentState()
}

sealed class HomeFragmentEvent {
    object OnSwipeRefresh : HomeFragmentEvent()
}

sealed class HomeViewEffect{

}