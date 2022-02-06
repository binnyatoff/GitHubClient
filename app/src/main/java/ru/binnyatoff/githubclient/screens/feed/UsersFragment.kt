package ru.binnyatoff.githubclient.screens.feed

import android.os.Bundle
import android.util.Log
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
import ru.binnyatoff.githubclient.models.User
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.feed.adapter.UsersAdapter
import ru.binnyatoff.githubclient.screens.feed.adapter.ClickDelegate

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users) {
    private val usersViewModel: UsersViewModel by viewModels()
    private val adapter = UsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //меню
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressCircular: ProgressBar = view.findViewById(R.id.progress_circular)
        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiper)//swipe to refresh
        val ufo: LinearLayout = view.findViewById(R.id.ufo)

        swiper.setOnRefreshListener {
            usersViewModel.refreshUsers()
        }

        observers(progressCircular, swiper, ufo)
        recyclerView(recyclerView)
    }

    private fun recyclerView(recyclerView: RecyclerView) {
        adapter.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(R.id.action_UsersFragment_to_UserDetailFragment, bundle)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observers(
        progressCircular: ProgressBar,
        swiper: SwipeRefreshLayout,
        ufo: LinearLayout
    ) {
        usersViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            swiper.isRefreshing = false
        }

        usersViewModel.searchList.observe(viewLifecycleOwner) {
            adapter.setData(it.items)
            swiper.isRefreshing = false
            if (it.items.isEmpty()){
                ufo.visibility = View.VISIBLE
            }
        }

        usersViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it) Toast.makeText(context, R.string.fale, Toast.LENGTH_SHORT).show()
        }

        usersViewModel.loading.observe(viewLifecycleOwner) {
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
                usersViewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //usersViewModel.search(newText)// отключено что-б не получить бан за кучу запросов на сервер
                //надо написать отдельную вьюху для поиска по гиту, а эту оставить для поиска по тому, что загружено
                return true
            }
        })
        searchView.setOnCloseListener {
            usersViewModel.getAllUsers()
            true
        }
    }
}