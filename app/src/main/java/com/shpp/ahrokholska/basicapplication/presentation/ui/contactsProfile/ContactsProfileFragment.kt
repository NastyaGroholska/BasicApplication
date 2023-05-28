package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentContactsProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_CAREER
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_IMAGE
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_USER_NAME
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsProfileFragment :
    BaseFragment<FragmentContactsProfileBinding>(FragmentContactsProfileBinding::inflate) {
    private val args: ContactsProfileFragmentArgs by navArgs()
    private val viewModel: ContactsProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        viewModel.getContactWithId(args.contactId)
        postponeEnterTransition()
    }

    override fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contact.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { contact ->
                contact?.let { bindThisToContact(it) }
            }
        }
    }

    override fun setListeners() {
        binding.myContactsImageArrow.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun bindThisToContact(contact: Contact) {
        with(binding) {
            textName.text = contact.name
            textCareer.text = contact.career
            imageProfile.loadFromURL(contact.picture)

            imageProfile.transitionName = TRANSITION_NAME_IMAGE + args.contactId
            textName.transitionName = TRANSITION_NAME_USER_NAME + args.contactId
            textCareer.transitionName = TRANSITION_NAME_CAREER + args.contactId
        }
        startPostponedEnterTransition()
    }
}