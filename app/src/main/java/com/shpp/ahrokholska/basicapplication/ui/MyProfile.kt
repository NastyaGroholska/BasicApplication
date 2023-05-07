package com.shpp.ahrokholska.basicapplication.ui

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.shpp.ahrokholska.basicapplication.utils.Constants.USER_NAME
import com.shpp.ahrokholska.basicapplication.databinding.ActivityMyProfileBinding
import com.shpp.ahrokholska.basicapplication.ui.contacts.MyContacts

class MyProfile : AppCompatActivity() {
    private val binding: ActivityMyProfileBinding by lazy {
        ActivityMyProfileBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textName.text = intent.getStringExtra(USER_NAME)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Slide()
        }

        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.btnViewMyContacts.setOnClickListener {
            openMyContacts()
        }
    }

    private fun openMyContacts() {
        startActivity(Intent(this, MyContacts::class.java))
    }

}