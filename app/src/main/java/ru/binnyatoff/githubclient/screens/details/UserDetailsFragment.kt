package ru.binnyatoff.githubclient.screens.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.databinding.FragmentUserDetailsBinding
import ru.binnyatoff.githubclient.repository.models.UserDetails

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val userDetailsViewModel: UserDetailsViewModel by viewModels()
    private val binding: FragmentUserDetailsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userLogin = arguments?.getString("currentUser")
        userDetailsViewModel.obtainEvent(event = UserDetailsEvent.ScreenInit(userLogin))

        userDetailsViewModel.userDetailsState.observe(viewLifecycleOwner) { state ->
            getState(state)
        }


        val bundle = bundleOf("user" to userLogin)
        binding.cardFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.action_userDeatailsFragment_to_followersFragment,
                bundle
            )
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
        with(binding) {
            avatar.visibility = View.GONE
            card.visibility = View.GONE
            cardFollowers.visibility = View.GONE
            ufoDetails.visibility = View.GONE
            cardLocation.visibility = View.GONE
            progressCircular.visibility = View.VISIBLE
        }
    }

    private fun isLoadedWithName(userDetails: UserDetails) {
        with(binding) {
            followersAmount.text = userDetails.followers.toString()
            location.text = userDetails.location
            userLogin.text = userDetails.login
            userName.text = userDetails.name
            publicRepos.text = userDetails.public_repos.toString()
            updated.text = userDetails.updated_at
            created.text = userDetails.created_at

            userName.visibility = View.VISIBLE
            userLogin.visibility = View.VISIBLE
            progressCircular.visibility = View.GONE
            avatar.visibility = View.VISIBLE
            card.visibility = View.VISIBLE
            cardFollowers.visibility = View.VISIBLE
            ufoDetails.visibility = View.GONE
            cardLocation.visibility = View.VISIBLE

            getAvatar(requireView(), userDetails.avatar_url, binding.avatar)
        }
    }

    private fun isLoadedWithoutName(userDetails: UserDetails) {
        with(binding) {
            followersAmount.text = userDetails.followers.toString()
            location.text = userDetails.location
            userLogin.text = userDetails.login
            userName.text = userDetails.name
            publicRepos.text = userDetails.public_repos.toString()
            updated.text = userDetails.updated_at
            created.text = userDetails.created_at

            userName.visibility = View.GONE
            userNameText.visibility = View.GONE
            userLogin.visibility = View.VISIBLE
            progressCircular.visibility = View.GONE
            avatar.visibility = View.VISIBLE
            card.visibility = View.VISIBLE
            cardFollowers.visibility = View.VISIBLE
            ufoDetails.visibility = View.VISIBLE
            cardLocation.visibility = View.VISIBLE

            getAvatar(requireView(), userDetails.avatar_url, binding.avatar)
        }
    }

    private fun isError() {
        with(binding) {
            ufoDetails.visibility = View.GONE
            card.visibility = View.GONE
            cardFollowers.visibility = View.GONE
            cardLocation.visibility = View.GONE
            progressCircular.visibility = View.GONE
        }
    }

    private fun isEmpty() {
        with(binding) {
            ufoDetails.visibility = View.VISIBLE
            card.visibility = View.GONE
            cardFollowers.visibility = View.GONE
            cardLocation.visibility = View.GONE
            progressCircular.visibility = View.GONE
        }
    }


    private fun getAvatar(view: View, avatar_url: String, avatar: ImageView) {
        Glide.with(view)
            .load(avatar_url)
            .circleCrop()
            .into(avatar)
    }
}