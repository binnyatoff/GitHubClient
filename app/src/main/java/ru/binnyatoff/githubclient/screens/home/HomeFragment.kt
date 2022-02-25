package ru.binnyatoff.githubclient.screens.home

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.data.models.User
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.SearchToList
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class HomeFragment() : SearchToList(R.layout.fragment_users) {

    private val usersViewModel: HomeViewModel by viewModels()
    private val adapter = getMyAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //меню
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressCircular: ProgressBar = view.findViewById(R.id.progress_circular)
        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiper)//swipe to refresh

        swiper.setOnRefreshListener {
            usersViewModel.refreshUsers()
        }

        recyclerView(recyclerView)
        observers(progressCircular, swiper)
    }

    private fun recyclerView(recyclerView: RecyclerView) {
        adapter.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(
                    R.id.action_homeFragment_to_userDeatailsFragment,
                    bundle
                )
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observers(
        progressCircular: ProgressBar,
        swiper: SwipeRefreshLayout,
    ) {
        usersViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            swiper.isRefreshing = false
        }

        usersViewModel.loading.observe(viewLifecycleOwner) {
            if (!it) {
                swiper.isRefreshing = it
                progressCircular.visibility = View.GONE
            }
        }
    }

}