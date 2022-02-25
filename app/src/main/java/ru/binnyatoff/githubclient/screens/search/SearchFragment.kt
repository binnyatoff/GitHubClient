package ru.binnyatoff.githubclient.screens.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.data.models.User
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModels()
    private val adapter = Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView: SearchView = view.findViewById(R.id.search_view)
        val recycler_search: RecyclerView = view.findViewById(R.id.recycler_search)
        val ufo: ImageView = view.findViewById(R.id.ufo)
        val ufo_text: TextView = view.findViewById(R.id.ufo_text)
        recycler_search.adapter = adapter
        recycler_search.layoutManager = LinearLayoutManager(context)
        searchView.isIconifiedByDefault = false


        searchViewModel.searchList.observe(viewLifecycleOwner) {
            Log.e("TAG", it.items.toString())
            adapter.setData(it.items)
            if (it.items.isEmpty()) {
                ufo.isVisible = true
                ufo_text.isVisible = true
            } else {
                ufo.isVisible = false
                ufo_text.isVisible = false

            }
            adapter.attachDelegate(object : ClickDelegate {
                override fun onClick(currentUser: User) {
                    val bundle = bundleOf("currentUser" to currentUser)
                    findNavController().navigate(
                        R.id.action_searchFragment_to_userDeatailsFragment,
                        bundle
                    )
                }
            })
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //searchViewModel.search(newText)
                return true
            }
        })
    }
}