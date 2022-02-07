package ru.binnyatoff.githubclient.screens.adapter

import ru.binnyatoff.githubclient.data.models.User

interface ClickDelegate {
    fun onClick(currentUser: User)
}