package com.picpay.desafio.android.presentation.scenes.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.FragmentUserListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    companion object {
        fun newInstance() = UserListFragment()
    }

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

        binding.lifecycleOwner = this
        binding.viewModel = userListViewModel

        val adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userListViewModel.users.observe(this) {
            adapter.users = it
        }

        userListViewModel.message.observe(this) { event ->
            event.executeIfNotHandled {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}