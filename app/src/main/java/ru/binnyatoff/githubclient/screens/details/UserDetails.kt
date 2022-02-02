package ru.binnyatoff.githubclient.screens.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.models.User

@AndroidEntryPoint
class UserDetails : Fragment(R.layout.fragment_user_details) {
    private val userDetailsViewModel:UserDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login: TextView = view.findViewById(R.id.user_login)
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val followers: CardView = view.findViewById(R.id.card_followers)
        val followers_amount: TextView = view.findViewById(R.id.followers_amount)
        val currentUser = arguments?.getParcelable<User>("currentUser")

        Glide.with(view)
            .load(currentUser?.avatar_url)
            .circleCrop()
            .into(avatar)

        currentUser?.login?.let { userDetailsViewModel.getUserDeatails(it)}

        val bundle = bundleOf("user" to currentUser?.login)
        login.text = currentUser?.login
        followers.setOnClickListener {
            findNavController().navigate(R.id.action_UserDetail_to_Followers, bundle)
        }

        userDetailsViewModel.userDetails.observe(viewLifecycleOwner) {
            followers_amount.text = it.followers.toString()
        }
    }
}