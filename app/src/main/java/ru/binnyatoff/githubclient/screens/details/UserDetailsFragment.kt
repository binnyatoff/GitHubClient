package ru.binnyatoff.githubclient.screens.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.models.User

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {
    private val userDetailsViewModel: UserDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = arguments?.getParcelable<User>("currentUser")
        currentUser?.login?.let { userDetailsViewModel.getUserDeatails(it) }

        val login: TextView = view.findViewById(R.id.user_login)
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val followers: CardView = view.findViewById(R.id.card_followers)
        val followers_amount: TextView = view.findViewById(R.id.followers_amount)
        val location: TextView = view.findViewById(R.id.location)
        val user_name: TextView = view.findViewById(R.id.user_name)
        val user_name_text: TextView = view.findViewById(R.id.user_name_text)
        val ufo: LinearLayout = view.findViewById(R.id.ufo_details)
        val card: MaterialCardView = view.findViewById(R.id.card)
        val card_followers: MaterialCardView = view.findViewById(R.id.card_followers)
        val card_location: MaterialCardView = view.findViewById(R.id.card_location)


        val bundle = bundleOf("user" to currentUser?.login)
        followers.setOnClickListener {
            findNavController().navigate(R.id.action_UserDetailFragment_to_FollowersFragment, bundle)
        }

        fun name_null(name: String) {
            if (name == null) {
                user_name.visibility = View.GONE
                user_name_text.visibility = View.GONE
            } else {
                user_name.text = name
            }
        }//Бывают аккаунты без имени

        userDetailsViewModel.userDetails.observe(viewLifecycleOwner) {

            followers_amount.text = it.followers.toString()
            location.text = it.location
            name_null(it.name)
            login.text = it.login
            getAvatar(view, it.avatar_url, avatar)

        }
        userDetailsViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it) {
                ufo.visibility = View.VISIBLE
                card.visibility = View.GONE
                card_followers.visibility = View.GONE
                card_location.visibility = View.GONE
            }
        }
    }


    fun getAvatar(view: View, avatar_url: String, avatar: ImageView) {
        Glide.with(view)
            .load(avatar_url)
            .circleCrop()
            .into(avatar)
    }
}