package ru.binnyatoff.githubclient.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.binnyatoff.githubclient.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class GitDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}