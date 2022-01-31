package ru.binnyatoff.githubclient.screens

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.retrofit.User

class UserDetails:Fragment(R.layout.fragment_user_details,) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login:TextView = view.findViewById(R.id.user_login)
        val avatar:ImageView = view.findViewById(R.id.avatar)
        val followers: Button = view.findViewById(R.id.followers)
        val currentUser = arguments?.getParcelable<User>("currentUser")
        val bundle = bundleOf("user" to currentUser?.login)
        login.text = currentUser?.login

        Glide.with(view)
            .load(currentUser?.avatar_url)
            .circleCrop()
            .into(avatar)

        followers.setOnClickListener{
            findNavController().navigate(R.id.action_UserDetail_to_Followers, bundle)
        }
    }
}