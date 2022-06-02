package ru.binnyatoff.githubclient.screens.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.databinding.FragmentUserDetailsBinding
import ru.binnyatoff.githubclient.repository.models.User
import ru.binnyatoff.githubclient.repository.models.UserDetails

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {
    private val userDetailsViewModel: UserDetailsViewModel by viewModels()
    private val binding: FragmentUserDetailsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = arguments?.getParcelable<User>("currentUser")
        currentUser?.login?.let { userDetailsViewModel.getUserDetails(it) }

        val bundle = bundleOf("user" to currentUser?.login)
        binding.cardFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.action_userDeatailsFragment_to_followersFragment,
                bundle
            )
        }

        userDetailsViewModel.userDetailsState.observe(viewLifecycleOwner) { state ->
            getState(state)
        }
    }

    private fun getState(state: UserDetailsState) {
        when (state) {
            is UserDetailsState.Loading -> isLoading()
            is UserDetailsState.LoadedWithName -> isLoadedWithName(state.userDetails)
            is UserDetailsState.LoadedWithoutName -> isLoadedWithoutName(state.userDetails)
            is UserDetailsState.Empty -> isEmpty()
            is UserDetailsState.Error -> isError()
        }
    }

    private fun isLoading() {

    }

    private fun isLoadedWithName(userDetails: UserDetails) {
        with(binding) {
            followersAmount.text = userDetails.followers.toString()
            location.text = userDetails.location
            userLogin.text = userDetails.login
            publicRepos.text = userDetails.public_repos.toString()
            updated.text = userDetails.updated_at
            created.text = userDetails.created_at
            userLogin.visibility = View.VISIBLE
            getAvatar(requireView(), userDetails.avatar_url, binding.avatar)
        }
    }

    private fun isLoadedWithoutName(userDetails: UserDetails) {
        with(binding) {
            followersAmount.text = userDetails.followers.toString()
            location.text = userDetails.location
            userLogin.text = userDetails.login
            userLogin.visibility = View.GONE
            publicRepos.text = userDetails.toString()
            updated.text = userDetails.updated_at
            created.text = userDetails.created_at
            getAvatar(requireView(), userDetails.avatar_url, binding.avatar)
        }
    }

    private fun isError() {
        with(binding) {
            ufoDetails.visibility = View.VISIBLE
            card.visibility = View.GONE
            cardFollowers.visibility = View.GONE
            cardLocation.visibility = View.GONE
        }
    }

    private fun isEmpty() {
        with(binding) {
            ufoDetails.visibility = View.VISIBLE
            card.visibility = View.GONE
            cardFollowers.visibility = View.GONE
            cardLocation.visibility = View.GONE
        }
    }


    private fun getAvatar(view: View, avatar_url: String, avatar: ImageView) {
        Glide.with(view)
            .load(avatar_url)
            .circleCrop()
            .into(avatar)
    }
}