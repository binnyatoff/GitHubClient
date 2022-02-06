package ru.binnyatoff.githubclient.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.binnyatoff.githubclient.models.User

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: List<User>)

    @Query("SELECT * FROM data")
    suspend fun readAllData(): List<User>
}