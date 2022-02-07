package ru.binnyatoff.githubclient.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "data")
data class User(
    @PrimaryKey
    val id:String,
    @ColumnInfo(name = "login")
    val login:String,
    @ColumnInfo(name = "avatar_url")
    val avatar_url:String
):Parcelable