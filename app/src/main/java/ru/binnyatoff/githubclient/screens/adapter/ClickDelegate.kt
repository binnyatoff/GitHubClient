package ru.binnyatoff.githubclient.screens.adapter

import ru.binnyatoff.githubclient.repository.models.User

interface ClickDelegate {
    fun onClick(currentUser: User)
}