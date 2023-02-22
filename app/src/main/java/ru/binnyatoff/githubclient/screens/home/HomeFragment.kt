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
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.databinding.FragmentHomeBinding
import ru.binnyatoff.githubclient.screens.adapter.SearchToList
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class HomeFragment : SearchToList(R.layout.fragment_home) {

    private var adapter: Adapter? = Adapter()
    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //меню
        viewModel.obtainEvent(event = HomeFragmentEvent.HomeInit)

        with(binding) {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())

            swiper.setOnRefreshListener {
                viewModel.obtainEvent(event = HomeFragmentEvent.ReloadScreen)
            }
        }

        adapter?.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser.login)
                findNavController().navigate(
                    R.id.action_homeFragment_to_userDeatailsFragment,
                    bundle
                )
            }
        })

        viewModel.homeViewState.observe(viewLifecycleOwner) { state ->
            setState(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    private fun setState(state: HomeFragmentState) {
        when (state) {
            is HomeFragmentState.Loading -> {
                binding.recyclerview.visibility = View.GONE
                binding.swiper.isRefreshing = false
                binding.progressCircular.visibility = View.VISIBLE
            }
            is HomeFragmentState.Empty -> {
                binding.recyclerview.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
            }
            is HomeFragmentState.Error -> {
                binding.recyclerview.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
                Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show()
            }
            is HomeFragmentState.Loaded -> {
                binding.swiper.isRefreshing = false
                binding.recyclerview.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                adapter?.setData(state.userList)
            }
        }
    }
}