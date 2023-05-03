package com.shpp.ahrokholska.basicapplication

import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.shpp.ahrokholska.basicapplication.databinding.MyProfileBinding

class MyProfile : AppCompatActivity() {
    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyProfileBinding.inflate(layoutInflater)
        binding.textName.text = intent.getStringExtra(SignUp.USER_NAME)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Slide()
        }

        setContentView(binding.root)
    }
}