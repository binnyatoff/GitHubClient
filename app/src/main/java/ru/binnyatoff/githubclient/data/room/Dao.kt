package ru.binnyatoff.githubclient.data.room

import androidx.room.*
import androidx.room.Dao
import ru.binnyatoff.githubclient.data.models.User

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: List<User>)

    @Query("SELECT * FROM data")
    suspend fun readAllData(): List<User>

    @Query("DELETE FROM data")
    suspend fun delete()
}