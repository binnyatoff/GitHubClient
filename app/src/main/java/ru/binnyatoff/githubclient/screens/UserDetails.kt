package ru.binnyatoff.githubclient.screens

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.retrofit.User

class UserDetails:Fragment(R.layout.fragment_user_details,) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id:TextView = view.findViewById(R.id.user_id)
        val login:TextView = view.findViewById(R.id.user_login)
        val avatar:ImageView = view.findViewById(R.id.avatar)
        val currentUser = arguments?.getParcelable<User>("currentUser")

        id.text =currentUser?.id.toString()
        login.text = currentUser?.login

        Glide.with(view)
            .load(currentUser?.avatar_url)
            .circleCrop()
            .into(avatar)
    }
}