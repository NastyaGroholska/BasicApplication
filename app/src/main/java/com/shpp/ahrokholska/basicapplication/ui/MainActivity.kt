package com.shpp.ahrokholska.basicapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.shpp.ahrokholska.basicapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
    }
    private val viewModel: UserViewModel by viewModels()
    private val navController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}