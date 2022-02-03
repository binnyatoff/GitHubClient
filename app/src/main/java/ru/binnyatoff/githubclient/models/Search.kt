package ru.binnyatoff.githubclient.models

import ru.binnyatoff.githubclient.models.User

data class Search(
    val total_count: Int,
    val items: List<User>
)