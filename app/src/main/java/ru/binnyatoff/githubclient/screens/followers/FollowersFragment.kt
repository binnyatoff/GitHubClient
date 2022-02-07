package ru.binnyatoff.githubclient.screens.followers

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.data.models.User
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class FollowersFragment : Fragment(R.layout.fragment_users) {
    private val followersViewModel: FollowersViewModel by viewModels()
    private val adapter = Adapter()

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
                findNavController().navigate(R.id.action_FollowersFragment_to_UserDetailFragment, bundle)
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
        followersViewModel.userList.observe(viewLifecycleOwner) {
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