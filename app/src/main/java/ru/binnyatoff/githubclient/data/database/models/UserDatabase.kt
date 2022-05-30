package ru.binnyatoff.githubclient.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class UserDatabase(
    @PrimaryKey
    val id:String,
    @ColumnInfo(name = "login")
    val login:String,
    @ColumnInfo(name = "avatar_url")
    val avatar_url:String
)