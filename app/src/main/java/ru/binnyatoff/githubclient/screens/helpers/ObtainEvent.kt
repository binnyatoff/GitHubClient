package ru.binnyatoff.githubclient.screens.helpers

interface EventHandler<T> {
    fun obtainEvent(event: T)
}