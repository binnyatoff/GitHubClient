package ru.binnyatoff.githubclient.repository.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val avatar_url: String,
    val followers: Int,
    val location: String,
    val login: String,
    var name: String?,
    var public_repos:Int,
    var updated_at:String,
    var created_at:String
): Parcelable