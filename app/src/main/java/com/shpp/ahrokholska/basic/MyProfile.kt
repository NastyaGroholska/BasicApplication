package com.shpp.ahrokholska.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.shpp.ahrokholska.basic.databinding.MyProfileBinding

class MyProfile : ComponentActivity() {
    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.name.text=intent.getStringExtra(SignUp.USER_NAME)
    }
}
