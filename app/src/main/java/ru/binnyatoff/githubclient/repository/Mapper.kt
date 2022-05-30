package ru.binnyatoff.githubclient.repository

import ru.binnyatoff.githubclient.data.network.models.UserDetailsNetwork
import ru.binnyatoff.githubclient.data.network.models.UserNetwork
import ru.binnyatoff.githubclient.repository.models.User
import ru.binnyatoff.githubclient.repository.models.UserDetails

fun UserNetwork.toUser(): User {
    return User(
        id = id,
        login = login,
        avatar_url = avatar_url
    )
}

fun UserDetailsNetwork.toUserDetails(): UserDetails {
    return UserDetails(
        avatar_url = avatar_url,
        followers = followers,
        location = location,
        login = login,
        name = name,
        public_repos = public_repos,
        updated_at = updated_at,
        created_at = created_at
    )
}