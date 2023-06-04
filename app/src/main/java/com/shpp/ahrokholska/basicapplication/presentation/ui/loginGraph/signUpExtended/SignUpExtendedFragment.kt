package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph.signUpExtended

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignUpExtendedBinding
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Parser
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.invisible
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {
    private val viewModel: SignUpExtendedViewModel by viewModels()
    private val args: SignUpExtendedFragmentArgs by navArgs()
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }

    override fun setListeners() {
        with(binding) {
            val name = InputHandler(
                tietUserName, tilUserName,
                getString(R.string.incorrect_user_name), Validator::isUserNameValid
            )
            tietMobilePhone.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
            val phone = InputHandler(
                tietMobilePhone, tilMobilePhone,
                getString(R.string.incorrect_phone_number),
                Validator::isPhoneNumberValid
            )
            btnCancel.setOnClickListener {
                navController.navigateUp()
            }
            setRegisterButtonListener(name, phone)
        }
    }

    private fun setRegisterButtonListener(name: InputHandler, phone: InputHandler) {
        binding.btnForward.setOnClickListener {
            val isNameValid = name.processInput()
            val isPhoneValid = phone.processInput()

            if (isNameValid && isPhoneValid) {
                sendCreateRequest()
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                    .setAnchorView(it).show()
            }
        }
    }

    override fun setObservers() {
        binding.tietUserName.append(Parser.getUserName(args.email))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.networkResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                with(binding) {
                    setLoading(false)
                    when (it) {
                        is NetworkResponse.Success -> navController.navigate(
                            SignUpExtendedFragmentDirections.actionSignUpToMyProfile()
                        )

                        is NetworkResponse.NetworkError -> {
                            AlertDialog.Builder(root.context)
                                .setMessage(getString(R.string.network_error))
                                .setPositiveButton(getString(R.string.retry)) { _, _ ->
                                    sendCreateRequest()
                                }.create().show()
                        }

                        is NetworkResponse.InputError -> {
                            AlertDialog.Builder(root.context)
                                .setMessage(getString(R.string.sign_up_error)).create().show()
                        }
                    }
                }
            }
        }
    }

    private fun sendCreateRequest() {
        setLoading(true)
        with(binding) {
            if (args.rememberMe) {
                viewModel.createAndSaveUser(
                    args.email, args.password,
                    tietUserName.text.toString(), tietMobilePhone.text.toString()
                )
            } else {
                viewModel.createUser(
                    args.email, args.password,
                    tietUserName.text.toString(), tietMobilePhone.text.toString()
                )
            }
        }
    }

    private fun setLoading(isStarted: Boolean) {
        if (isStarted) {
            binding.signUpExtendedProgressWindow.visible()
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        } else {
            binding.signUpExtendedProgressWindow.invisible()
            callback.remove()
        }
    }
}