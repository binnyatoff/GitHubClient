package ru.binnyatoff.githubclient.screens.followers

import ru.binnyatoff.githubclient.repository.models.User

sealed class FollowersFragmentState {
    object Loading: FollowersFragmentState()
    data class Loaded(val followersList:List<User>): FollowersFragmentState()
    object Empty: FollowersFragmentState()
    data class Error(val error: String):FollowersFragmentState()
}

sealed class FollowersFragmentEvent{
    data class HomeInit(val user: String): FollowersFragmentEvent()
}