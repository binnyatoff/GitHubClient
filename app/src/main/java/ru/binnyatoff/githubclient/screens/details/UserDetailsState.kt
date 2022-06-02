package ru.binnyatoff.githubclient.screens.details

import ru.binnyatoff.githubclient.repository.models.UserDetails

sealed class UserDetailsState {
    object Loading : UserDetailsState()
    data class LoadedWithName(var userDetails: UserDetails) : UserDetailsState()
    data class LoadedWithoutName(var userDetails: UserDetails) : UserDetailsState()
    object Empty : UserDetailsState()
    data class Error(var error: String) : UserDetailsState()
}
