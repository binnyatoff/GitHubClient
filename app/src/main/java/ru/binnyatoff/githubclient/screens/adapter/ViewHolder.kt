package ru.binnyatoff.githubclient.screens.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.repository.models.User

class ViewHolder(itemView: View, private val delegate: ClickDelegate?) :
    RecyclerView.ViewHolder(itemView) {

    var login: TextView = itemView.findViewById(R.id.login)
    var avatar: ImageView = itemView.findViewById(R.id.avatar)
    var context: Context = itemView.context

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