package ru.binnyatoff.githubclient.screens.feed.adapter

import ru.binnyatoff.githubclient.models.User

interface ClickDelegate {
    fun onClick(currentUser: User)
}