package ru.binnyatoff.githubclient.data.database

import androidx.room.*
import androidx.room.Dao
import ru.binnyatoff.githubclient.data.database.models.UserDatabase

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: List<UserDatabase>)

    @Query("SELECT * FROM data")
    suspend fun readAllData(): List<UserDatabase>

    @Query("DELETE FROM data")
    suspend fun delete()
}