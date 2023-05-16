package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentContactsProfileBinding
import com.shpp.ahrokholska.basicapplication.ui.contacts.ContactsViewModel
import com.shpp.ahrokholska.basicapplication.utils.ext.loadFromURL

class ContactsProfile : Fragment() {
    private var _binding: FragmentContactsProfileBinding? = null
    private val binding get() = _binding!!
    private val args: ContactsProfileArgs by navArgs()
    private val navController by lazy { findNavController() }
    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindThisToContact(viewModel.getContactWithId(args.contactId))
        binding.myContactsImageArrow.setOnClickListener {
            navController.navigate(R.id.action_contactsProfile_to_myContacts)
        }
    }

    private fun bindThisToContact(contact: Contact) {
        with(binding) {
            textName.text = contact.name
            textCareer.text = contact.career
            imageProfile.loadFromURL(contact.picture)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}