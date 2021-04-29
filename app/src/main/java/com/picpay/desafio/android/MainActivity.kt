package com.picpay.desafio.android

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = UserListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        mainViewModel.screenState.observe(this) {
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
