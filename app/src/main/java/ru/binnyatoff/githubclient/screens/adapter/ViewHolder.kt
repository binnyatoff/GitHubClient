package ru.binnyatoff.githubclient.screens.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.binnyatoff.githubclient.databinding.UserItemBinding
import ru.binnyatoff.githubclient.repository.models.User

class ViewHolder(itemView: UserItemBinding, private val delegate: ClickDelegate?) :
    RecyclerView.ViewHolder(itemView.root) {

    var login: TextView = itemView.login
    var avatar: ImageView = itemView.avatar
    var context: Context = itemView.root.context

    fun bind(currentUser: User) {
        val url: String = currentUser.avatar_url
        login.text = currentUser.login
        getImage(url)
        itemView.setOnClickListener {
            delegate?.onClick(currentUser)
        }
    }

    private fun getImage(url: String) {
        Glide
            .with(context)
            .load(url)
            .circleCrop()
            .into(avatar)
    }
}