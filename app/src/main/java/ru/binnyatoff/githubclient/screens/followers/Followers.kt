package ru.binnyatoff.githubclient.screens.followers

import android.os.Bundle
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
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.feed.UsersAdapter
import ru.binnyatoff.githubclient.screens.feed.clickDelegate

@AndroidEntryPoint
class Followers : Fragment(R.layout.fragment_users) {
    private val followersViewModel: FollowersViewModel by viewModels()
    private val adapter = UsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressCircular: ProgressBar = view.findViewById(R.id.progress_circular)
        val user:String = arguments?.get("user").toString()
        observers(progressCircular, adapter)
        recyclerView(recyclerView)
        followersViewModel.getFollowers(user)
    }

    private fun recyclerView(recyclerView: RecyclerView) {
        adapter.attachDelegate(object : clickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(R.id.action_Followers_to_UserDetail, bundle)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observers(progressCircular: ProgressBar, adapter: UsersAdapter) {
        followersViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        followersViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        followersViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                progressCircular.visibility = View.VISIBLE
            } else {
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
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}