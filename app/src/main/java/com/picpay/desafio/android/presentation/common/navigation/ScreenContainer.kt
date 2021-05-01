package com.picpay.desafio.android.presentation.common.navigation

import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.FragmentScreen

interface ScreenContainer {
    val initialScreen: FragmentScreen
    fun setToolbarTitle(@StringRes titleId: Int)
}