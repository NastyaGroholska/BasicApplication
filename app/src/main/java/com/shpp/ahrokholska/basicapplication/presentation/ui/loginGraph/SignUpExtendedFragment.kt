package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignUpExtendedBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph.signIn.SignInViewModel
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Parser
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator


class SignUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {
    private val viewModel: SignInViewModel by hiltNavGraphViewModels(R.id.loginGraph)
    private val args: SignUpExtendedFragmentArgs by navArgs()

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
            // setRegisterButtonListener(email, password)
        }
    }

    override fun setObservers() {
        binding.tietUserName.append(Parser.getUserName(args.email))
    }
}