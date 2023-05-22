package com.shpp.ahrokholska.basicapplication.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shpp.ahrokholska.basicapplication.databinding.ActivityMainBinding

// TODO This is how it looks like now
//  class MainActivity : AppCompatActivity(R.layout.activity_main)
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}