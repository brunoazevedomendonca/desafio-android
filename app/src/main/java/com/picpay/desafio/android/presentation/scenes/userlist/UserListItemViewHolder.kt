package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.domain.model.User

class UserListItemViewHolder(
    private val listItemUserBinding: ListItemUserBinding
) : RecyclerView.ViewHolder(listItemUserBinding.root) {

    fun bind(user: User) {
        listItemUserBinding.user = user
    }
}