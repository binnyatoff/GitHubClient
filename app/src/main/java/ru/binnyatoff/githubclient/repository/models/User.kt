package ru.binnyatoff.githubclient.repository.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:String,
    val login:String,
    val avatar_url:String
):Parcelable