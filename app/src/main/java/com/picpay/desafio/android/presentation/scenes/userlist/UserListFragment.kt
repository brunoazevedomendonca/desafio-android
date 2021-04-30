package com.picpay.desafio.android.presentation.scenes.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.FragmentUserListBinding
import com.picpay.desafio.android.presentation.common.ScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private val userListViewModel: UserListViewModel by viewModel()

    private lateinit var binding: FragmentUserListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.swipeRefresh.setOnRefreshListener { userListViewModel.onRefresh() }

        userListViewModel.screenState.observe(this) {
            binding.swipeRefresh.isRefreshing = false

            when (it) {
                is ScreenState.Success -> {
                    adapter.users = it.data
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is ScreenState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Error -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        userListViewModel.refreshError.observe(this) { event ->
            event.executeIfNotHandled {
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

        }
    }
}