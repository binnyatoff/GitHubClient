package ru.binnyatoff.githubclient.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.databinding.FragmentSearchBinding
import ru.binnyatoff.githubclient.repository.models.SearchResult
import ru.binnyatoff.githubclient.repository.models.User
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModels()
    private val adapter = Adapter()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.searchFragmentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchFragmentState.Error -> Toast.makeText(
                    requireContext(),
                    state.e,
                    Toast.LENGTH_LONG
                ).show()
                is SearchFragmentState.Loaded -> loaded(state.searchResult)
                SearchFragmentState.Loading -> Toast.makeText(
                    requireContext(),
                    "Loading",
                    Toast.LENGTH_LONG
                ).show()// Сделать с этим что-нибудь
            }
        }

        with(binding) {
            recyclerSearch.adapter = adapter
            recyclerSearch.layoutManager = LinearLayoutManager(context)
            searchView.isIconifiedByDefault = false
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




        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.obtainEvent(SearchFragmentEvent.Search(query))
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //searchViewModel.search(newText)
                searchViewModel.obtainEvent(SearchFragmentEvent.Search(newText))
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loaded(searchResult: SearchResult) {
        adapter.setData(searchResult.items)
        if (searchResult.items.isEmpty()) {
            binding.ufo.isVisible = true
            binding.ufoText.isVisible = true
        } else {
            binding.ufo.isVisible = false
            binding.ufoText.isVisible = false

        }
    }
}