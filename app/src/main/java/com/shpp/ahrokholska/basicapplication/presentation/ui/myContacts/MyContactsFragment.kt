package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyContactsBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter.ContactsAdapter
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.SelectionListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsNormalViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.ui.pager.MyProfileAndMyContactsFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.pager.MyProfileAndMyContactsFragmentDirections
import com.shpp.ahrokholska.basicapplication.presentation.utils.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.enableHorizontalSwipe
import kotlinx.coroutines.launch

class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {
    private val viewModel: ContactsViewModel by viewModels()
    private val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter(
            object : ContactsNormalItemListener {
                override fun onBinClick(contact: Contact, position: Int) {
                    deleteRVItem(contact, position)
                }

                override fun onItemClick(
                    contact: Contact,
                    transitionPairs: Array<Pair<View, String>>
                ) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setObservers()
        setListeners()
    }

    private fun initRecycler() {
        binding.myContactsRvContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
            enableHorizontalSwipe<ContactsNormalViewHolder> {
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
        deleteMultipleRVItems(listOf(position to contact))
    }

    private fun deleteMultipleRVItems(contacts: List<Pair<Int, Contact>>) {
        contacts.forEach { viewModel.deleteContact(it.second) }
        Snackbar.make(
            binding.myContactsRvContacts, if (contacts.size == 1) R.string.removed_contact
            else R.string.removed_contacts, RV_TIME_TO_CANCEL_MS
        ).setAction(getString(R.string.undo).uppercase()) {
            contacts.forEach { viewModel.insertContact(it.second, it.first) }
        }.setAnchorView(binding.myContactsRvContacts).show()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.contacts.collect { list ->
                        contactsAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.multiselectState.collect {
                        binding.myContactsBinBtn.visibility = if (it) View.VISIBLE else View.GONE
                        contactsAdapter.changeMultiselectState(it)
                    }
                }
                launch {
                    viewModel.selectedPositions.collect {
                        contactsAdapter.changeMultiselectItems(it)
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.myContactsTextAdd.setOnClickListener {
            navController.navigate(R.id.action_myContacts_to_addContactDialogFragment)
        }
        binding.myContactsImageArrow.setOnClickListener {
            (parentFragment as? MyProfileAndMyContactsFragment)?.openTab(0)
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
