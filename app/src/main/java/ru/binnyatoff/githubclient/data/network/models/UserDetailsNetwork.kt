package ru.binnyatoff.githubclient.data.network.models

data class UserDetailsNetwork(
    val avatar_url: String,
    val followers: Int,
    val location: String?,
    val login: String,
    var name: String?,
    var public_repos: Int,
    var updated_at: String,
    var created_at: String
)
