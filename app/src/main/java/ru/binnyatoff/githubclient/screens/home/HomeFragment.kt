package ru.binnyatoff.githubclient.screens.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.repository.models.User
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.databinding.FragmentHomeBinding
import ru.binnyatoff.githubclient.screens.SearchToList
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class HomeFragment : SearchToList(R.layout.fragment_home) {

    private lateinit var adapter: Adapter
    private val viewModel by viewModels<HomeViewModel>()
    private val binding: FragmentHomeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //меню
        adapter = Adapter()
        adapter.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser)
                findNavController().navigate(
                    R.id.action_homeFragment_to_userDeatailsFragment,
                    bundle
                )
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
            swiper.setOnRefreshListener {
                viewModel.refreshUsers()
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            getState(state)
        }
    }

    private fun getState(state: HomeFragmentState) {
        when (state) {
            is HomeFragmentState.Loading -> {
                binding.swiper.isRefreshing = false
                binding.progressCircular.visibility = View.VISIBLE
            }
            is HomeFragmentState.Empty -> {
                binding.progressCircular.visibility = View.GONE
            }
            is HomeFragmentState.Error -> {
                binding.progressCircular.visibility = View.GONE
                Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show()
            }
            is HomeFragmentState.Loaded -> {
                binding.swiper.isRefreshing = false
                binding.progressCircular.visibility = View.GONE
                adapter.setData(state.userList)
            }
        }
    }
}