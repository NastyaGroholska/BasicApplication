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
import com.shpp.ahrokholska.basicapplication.ui.contacts.adapter.ContactsAdapter
import com.shpp.ahrokholska.basicapplication.ui.contacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.ui.contacts.interfaces.SelectionListener
import com.shpp.ahrokholska.basicapplication.ui.profile_and_contacts.MyProfileAndMyContactsFragment
import com.shpp.ahrokholska.basicapplication.ui.profile_and_contacts.MyProfileAndMyContactsFragmentDirections
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
            object : ContactsNormalItemListener {
                override fun onBinClick(contact: Contact, position: Int) {
                    deleteRVItem(contact, position)
                }

            override fun onItemClick(contact: Contact, transitionPairs: Array<Pair<View, String>>) {
                navController.navigate(
                    MyProfileAndMyContactsFragmentDirections.actionMyContactsToContactsProfile(
                        contact.id
                    ),
                    FragmentNavigatorExtras(*transitionPairs)
                )
            }
        }, object : SelectionListener {
                override fun clearSelection() {
                    viewModel.clearSelectedPositions()
                }

                override fun addItemToSelection(itemPos: Int) {
                    viewModel.addSelectedPosition(itemPos)
                }

                override fun removeItemFromSelection(itemPos: Int) {
                    viewModel.removeSelectedPosition(itemPos)
                }
            })
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
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
            enableHorizontalSwipe {
                val pos = it.adapterPosition
                deleteRVItem(contactsAdapter.currentList[pos], pos)
            }
            postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
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

    private fun deleteMultipleRVItems(contacts: List<Pair<Int, Contact>>) {
        contacts.forEach {
            viewModel.deleteContact(it.second)
        }
        Snackbar.make(
            binding.myContactsRvContacts, if (contacts.size == 1) R.string.removed_contact
            else R.string.removed_contacts, RV_TIME_TO_CANCEL_MS
        )
            .setAction(getString(R.string.undo).uppercase()) {
                contacts.forEach { viewModel.insertContact(it.second, it.first) }
            }.setAnchorView(binding.myContactsRvContacts).show()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.contacts.collect { list ->
                contactsAdapter.submitList(list)
            }
        }
        lifecycleScope.launch {
            viewModel.multiselectState.collect {
                binding.myContactsBinBtn.visibility = if (it) View.VISIBLE else View.GONE
                contactsAdapter.changeMultiselectState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.selectedPositions.collect {
                contactsAdapter.changeMultiselectItems(it)
            }
        }
    }

    private fun setListeners() {
        binding.myContactsTextAdd.setOnClickListener {
            navController.navigate(R.id.action_myContacts_to_addContactDialogFragment)
        }

        binding.myContactsImageArrow.setOnClickListener {
            (parentFragment as MyProfileAndMyContactsFragment).binding.pager.currentItem = 0
        }
        binding.myContactsBinBtn.setOnClickListener {
            deleteMultipleRVItems(contactsAdapter.deleteMultiselectItems())
            it.visibility = View.GONE
        }
    }

    companion object {
        private const val RV_ITEM_SPACE = 50
        private const val RV_TIME_TO_CANCEL_MS = 5 * 1000
    }
}