package ru.binnyatoff.githubclient.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User_Details(
    val avatar_url: String,
    val followers: Int,
    val location: String,
    val login: String,
    val name: String
): Parcelable