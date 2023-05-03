package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.shpp.ahrokholska.basicapplication.utils.Constants.USER_NAME
import com.shpp.ahrokholska.basicapplication.databinding.MyProfileBinding

class MyProfile : AppCompatActivity() {
    private val binding: MyProfileBinding by lazy { MyProfileBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textName.text = intent.getStringExtra(USER_NAME)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Slide()
        }

        setContentView(binding.root)
    }
}