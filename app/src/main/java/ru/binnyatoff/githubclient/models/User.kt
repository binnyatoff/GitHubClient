package ru.binnyatoff.githubclient.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login:String,
    val avatar_url:String
):Parcelable