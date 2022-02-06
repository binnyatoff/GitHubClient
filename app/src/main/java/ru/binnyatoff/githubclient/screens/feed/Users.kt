package ru.binnyatoff.githubclient.screens.feed

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.models.User
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.binnyatoff.githubclient.R

@AndroidEntryPoint
class Users : Fragment(R.layout.fragment_users) {
    private val usersViewModel: UsersViewModel by viewModels()
    private val adapter = UsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //меню
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressCircular: ProgressBar = view.findViewById(R.id.progress_circular)
        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiper)//swipe to refresh

        swiper.setOnRefreshListener {
            usersViewModel.refreshUsers()
        }

        observers(progressCircular, adapter, swiper)
        recyclerView(recyclerView)
    }

    private fun recyclerView(recyclerView: RecyclerView) {
        adapter.attachDelegate(object : clickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(R.id.action_Users_to_UserDetail, bundle)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observers(
        progressCircular: ProgressBar,
        adapter: UsersAdapter,
        swiper: SwipeRefreshLayout
    ) {
        usersViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            swiper.isRefreshing = false
        }

        usersViewModel.searchList.observe(viewLifecycleOwner) {
            adapter.setData(it.items)
            swiper.isRefreshing = false
        }

        usersViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        usersViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                progressCircular.visibility = View.VISIBLE
            } else {
                swiper.isRefreshing = it
                progressCircular.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                usersViewModel.search(query)
                Log.e("TAG", query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        searchView.setOnCloseListener {
            usersViewModel.getAllUsers()
            true
        }
    }
}