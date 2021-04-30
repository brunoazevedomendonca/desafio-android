package com.picpay.desafio.android.presentation.common.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.picpay.desafio.android.presentation.scenes.userlist.UserListFragment

fun userListScreen() = FragmentScreen {
    UserListFragment.newInstance()
}