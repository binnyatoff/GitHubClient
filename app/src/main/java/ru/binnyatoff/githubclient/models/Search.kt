package ru.binnyatoff.githubclient.models

data class Search(
    val total_count: Int,
    val items: List<User>
)