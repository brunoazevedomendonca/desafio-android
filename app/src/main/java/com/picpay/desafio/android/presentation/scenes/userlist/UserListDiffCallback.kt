package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.recyclerview.widget.DiffUtil
import com.picpay.domain.model.User

class UserListDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (oldList.size <= oldItemPosition || oldList.size <= newItemPosition) false
        else oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (oldList.size <= oldItemPosition || oldList.size <= newItemPosition) false
        else oldList[oldItemPosition] == newList[newItemPosition]
    }
}