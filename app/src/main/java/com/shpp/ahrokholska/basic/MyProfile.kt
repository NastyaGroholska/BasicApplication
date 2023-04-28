package com.shpp.ahrokholska.basic

import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.activity.ComponentActivity
import com.shpp.ahrokholska.basic.databinding.MyProfileBinding

class MyProfile : ComponentActivity() {
    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyProfileBinding.inflate(layoutInflater)
        binding.name.text = intent.getStringExtra(SignUp.USER_NAME)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Slide()
        }

        setContentView(binding.root)
    }
}
