package com.picpay.desafio.android.presentation.scenes.userlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.domain.model.User
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListItemViewHolder(
    private val listItemUserBinding: ListItemUserBinding
) : RecyclerView.ViewHolder(listItemUserBinding.root) {

    fun bind(user: User) {
        listItemUserBinding.name.text = user.name
        listItemUserBinding.username.text = user.userName
        listItemUserBinding.progressBar.visibility = View.VISIBLE
        Picasso.get()
            .load(user.image)
            .error(R.drawable.ic_round_account_circle)
            .into(listItemUserBinding.picture, object : Callback {
                override fun onSuccess() {
                    listItemUserBinding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    listItemUserBinding.progressBar.visibility = View.GONE
                }
            })
    }
}