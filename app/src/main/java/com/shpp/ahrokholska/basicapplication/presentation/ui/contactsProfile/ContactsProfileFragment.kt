package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentContactsProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.NavigationBaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_CAREER
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_IMAGE
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_USER_NAME
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL
import kotlinx.coroutines.launch

class ContactsProfileFragment : NavigationBaseFragment<FragmentContactsProfileBinding>() {
    private val args: ContactsProfileFragmentArgs by navArgs()
    private val viewModel: ContactsProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        viewModel.getContactWithId(args.contactId)
        postponeEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?):
            FragmentContactsProfileBinding {
        return FragmentContactsProfileBinding.inflate(inflater, container, false)
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contact.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { contact ->
                contact?.let { bindThisToContact(it) }
            }
        }
    }

    private fun setListeners() {
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