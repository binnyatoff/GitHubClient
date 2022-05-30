package ru.binnyatoff.githubclient.screens.followers

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.repository.models.User
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.SearchToList
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class FollowersFragment : SearchToList(R.layout.fragment_home) {
    private val followersViewModel: FollowersViewModel by viewModels()
    private val adapter = getMyAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressCircular: ProgressBar = view.findViewById(R.id.progress_circular)
        val user: String = arguments?.get("user").toString()
        val ufo = view.findViewById<LinearLayout>(R.id.ufo)
        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiper)//swipe to refresh
        observers(progressCircular, ufo, swiper)
        recyclerView(recyclerView)
        followersViewModel.getFollowers(user)
        swiper.setOnRefreshListener {
            followersViewModel.getFollowers(user)
        }
    }

    private fun recyclerView(recyclerView: RecyclerView) {
        adapter.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(
                    R.id.action_followersFragment_to_userDeatailsFragment,
                    bundle
                )
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observers(
        progressCircular: ProgressBar,
        ufo: LinearLayout,
        swiper: SwipeRefreshLayout
    ) {
        followersViewModel.followersList.observe(viewLifecycleOwner) {
            swiper.isRefreshing = false
            if (it.isEmpty()) {
                ufo.visibility = View.VISIBLE
            } else {
                ufo.visibility = View.GONE
                adapter.setData(it)
            }
        }

        followersViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        followersViewModel.loading.observe(viewLifecycleOwner) {
            if (!it) {
                swiper.isRefreshing = it
                progressCircular.visibility = View.GONE
            }
        }
    }
}