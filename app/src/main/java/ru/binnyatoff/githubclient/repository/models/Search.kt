package ru.binnyatoff.githubclient.repository.models

data class Search(
    val total_count: Int,
    val items: List<User>
)