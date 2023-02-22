package ru.binnyatoff.githubclient.repository.models

data class SearchResult(
    val total_count: Int,
    val items: List<User>
)