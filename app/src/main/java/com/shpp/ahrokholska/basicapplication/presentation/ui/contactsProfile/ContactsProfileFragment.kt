package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentContactsProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_CAREER
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_IMAGE
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_USER_NAME
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL
import kotlinx.coroutines.launch

class ContactsProfileFragment : Fragment() {
    private var _binding: FragmentContactsProfileBinding? = null
    private val binding get() = _binding!!
    private val args: ContactsProfileFragmentArgs by navArgs()
    private val navController by lazy { findNavController() }
    private val viewModel: DetailViewContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        viewModel.getContactWithId(args.contactId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.contact.collect { contact ->
                contact?.let { bindThisToContact(it) }
            }
        }
    }

    private fun setListeners() {
        binding.myContactsImageArrow.setOnClickListener {
            navController.popBackStack()
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
    }
}