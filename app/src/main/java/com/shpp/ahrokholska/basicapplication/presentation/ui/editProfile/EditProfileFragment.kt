package com.shpp.ahrokholska.basicapplication.presentation.ui.editProfile

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentEditProfileBinding
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.DATE_REQUEST_KEY
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Parser
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.invisible
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    private val viewModel: EditProfileViewModel by viewModels()
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }

    override fun setListeners() {
        with(binding) {
            tietPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
            val name = InputHandler(
                tietUsername, tilUsername,
                getString(R.string.incorrect_user_name), Validator::isUserNameValid
            )
            val phone = InputHandler(
                tietPhone, tilPhone,
                getString(R.string.incorrect_phone_number),
                Validator::isPhoneNumberValid
            )
            btnSave.setOnClickListener {
                val isNameValid = name.processInput()
                val isPhoneValid = phone.processInput()
                if (isNameValid && isPhoneValid) {
                    editUser()
                } else {
                    Snackbar.make(it, R.string.edit_user_error, Snackbar.LENGTH_SHORT)
                        .setAnchorView(it).show()
                }
            }
            imageArrow.setOnClickListener {
                navController.navigateUp()
            }
            setFragmentResultListener(DATE_REQUEST_KEY) { _, bundle ->
                val result = bundle.getString(DATE_REQUEST_KEY)
                binding.tietDate.setText(result)
            }
            tietDate.setOnClickListener {
                DatePickerFragment().show(parentFragmentManager, DATE_PICKER)
            }
        }
    }

    override fun setObservers() {
        with(binding) {
            tietUsername.setText(viewModel.user.name)
            tietCareer.setText(viewModel.user.career)
            tietPhone.setText(viewModel.user.phone)
            tietAddress.setText(viewModel.user.address)
            tietDate.setText(Parser.getDate(viewModel.user.birthday ?: ""))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.networkResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                with(binding) {
                    setLoading(false)
                    if (it is NetworkResponse.Success) {
                        navController.navigateUp()
                    } else {
                        AlertDialog.Builder(root.context)
                            .setMessage(getString(R.string.network_error))
                            .setPositiveButton(getString(R.string.retry)) { _, _ ->
                                editUser()
                            }.create().show()
                    }
                }
            }
        }
    }

    private fun setLoading(isStarted: Boolean) {
        if (isStarted) {
            binding.editUserProgressWindow.visible()
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        } else {
            binding.editUserProgressWindow.invisible()
            callback.remove()
        }
    }

    private fun editUser() {
        setLoading(true)
        with(binding) {
            viewModel.editUser(
                tietUsername.text.toString(), tietCareer.text.toString(),
                tietPhone.text.toString(), tietAddress.text.toString(), tietDate.text.toString()
            )
        }
    }

    private companion object {
        const val DATE_PICKER = "DATE_PICKER"
    }
}