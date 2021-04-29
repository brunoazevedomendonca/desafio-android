package com.picpay.desafio.android.presentation.scenes.userlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.scenes.common.ScreenState
import com.picpay.desafio.android.databinding.ActivityUserListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListActivity : AppCompatActivity(R.layout.activity_user_list) {

    private val userListViewModel: UserListViewModel by viewModel()

    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        userListViewModel.screenState.observe(this) {
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
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
