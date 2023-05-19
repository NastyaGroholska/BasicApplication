package com.shpp.ahrokholska.basicapplication.ui.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyContactsBinding
import com.shpp.ahrokholska.basicapplication.ui.shared.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.ui.viewmodels.ContactsViewModel
import com.shpp.ahrokholska.basicapplication.utils.ext.enableHorizontalSwipe
import kotlinx.coroutines.launch

class MyContacts : Fragment() {
    private var _binding: FragmentMyContactsBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val viewModel: ContactsViewModel by activityViewModels()
    private val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter(
            onBinClick = ::deleteRVItem,
            onItemClick = ::openContactsProfile
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view.context)
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(context: Context) {
        binding.myContactsRvContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(context)
            enableHorizontalSwipe {
                val pos = it.adapterPosition
                deleteRVItem(contactsAdapter.currentList[pos], pos)
            }
        }.addItemDecoration(
            VerticalSpaceItemDecoration(RV_ITEM_SPACE)
        )
    }

    private fun deleteRVItem(contact: Contact, position: Int) {
        viewModel.deleteContact(contact)
        Snackbar.make(binding.myContactsRvContacts, R.string.removed_contact, RV_TIME_TO_CANCEL_MS)
            .setAction(getString(R.string.undo).uppercase()) {
                viewModel.insertContact(contact, position)
            }.setAnchorView(binding.myContactsRvContacts).show()
    }

    private fun openContactsProfile(contact: Contact, transitionPairs:Array<Pair<View, String>>) {
        navController.navigate(
            MyContactsDirections.actionMyContactsToContactsProfile(contact.id),
            FragmentNavigatorExtras(*transitionPairs)
        )
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.contacts.collect { list ->
                contactsAdapter.submitList(list)
            }
        }
    }

    private fun setListeners() {
        binding.myContactsTextAdd.setOnClickListener {
            navController.navigate(R.id.action_myContacts_to_addContactDialogFragment)
        }

        binding.myContactsImageArrow.setOnClickListener {
            navController.navigate(R.id.action_myContacts_to_myProfile)
        }
    }

    companion object {
        private const val RV_ITEM_SPACE = 50
        private const val RV_TIME_TO_CANCEL_MS = 5 * 1000
    }
}