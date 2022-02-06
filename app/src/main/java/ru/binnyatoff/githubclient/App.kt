package ru.binnyatoff.githubclient

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import ru.binnyatoff.githubclient.room.GitDatabase

@HiltAndroidApp
class App:Application() {
}