package ru.binnyatoff.githubclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.binnyatoff.githubclient.data.database.models.UserDatabase

@Database(entities = [UserDatabase::class], version = 1, exportSchema = false)
abstract class GitDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}