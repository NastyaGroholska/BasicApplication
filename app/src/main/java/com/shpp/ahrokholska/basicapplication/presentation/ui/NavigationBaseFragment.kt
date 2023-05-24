package com.shpp.ahrokholska.basicapplication.presentation.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class NavigationBaseFragment<T : ViewBinding> : BaseFragment<T>() {
    protected val navController: NavController
        get() = findNavController()
}