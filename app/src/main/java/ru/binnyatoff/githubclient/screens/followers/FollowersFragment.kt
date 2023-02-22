package ru.binnyatoff.githubclient.screens.followers

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
import ru.binnyatoff.githubclient.screens.adapter.Adapter
import ru.binnyatoff.githubclient.screens.adapter.SearchToList
import ru.binnyatoff.githubclient.screens.adapter.ClickDelegate

@AndroidEntryPoint
class FollowersFragment : SearchToList(R.layout.fragment_home) {

    private val followersViewModel: FollowersViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: Adapter? = getMyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val user: String = arguments?.get("user").toString()

        followersViewModel.obtainEvent(FollowersFragmentEvent.HomeInit(user = user))

        adapter?.attachDelegate(object : ClickDelegate {
            override fun onClick(currentUser: User) {
                val bundle = bundleOf("currentUser" to currentUser.login)
                findNavController().navigate(
                    R.id.action_followersFragment_to_userDeatailsFragment,
                    bundle
                )
            }
        })

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        followersViewModel.followersViewState.observe(viewLifecycleOwner) { state ->
            setView(state)
        }

        binding.swiper.setOnRefreshListener {
            followersViewModel.obtainEvent(FollowersFragmentEvent.HomeInit(user = user))
        }
    }

    private fun setView(state: FollowersFragmentState?) {
        when (state) {
            is FollowersFragmentState.Loading -> loading()
            is FollowersFragmentState.Loaded -> loaded(state)
            is FollowersFragmentState.Empty -> empty()
            is FollowersFragmentState.Error -> error(state)
        }
    }

    private fun loading() {
        binding.recyclerview.visibility = View.GONE
        binding.swiper.isRefreshing = false
        binding.progressCircular.visibility = View.VISIBLE

    }

    private fun loaded(state: FollowersFragmentState.Loaded) {
        binding.swiper.isRefreshing = false
        binding.recyclerview.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
        adapter?.setData(state.followersList)
    }

    private fun empty() {
        binding.recyclerview.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
    }

    private fun error(state: FollowersFragmentState.Error) {
        binding.recyclerview.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
        Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}