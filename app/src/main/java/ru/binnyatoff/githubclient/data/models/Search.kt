package ru.binnyatoff.githubclient.data.models

data class Search(
    val total_count: Int,
    val items: List<User>
)