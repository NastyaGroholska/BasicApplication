package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
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
import com.shpp.ahrokholska.basicapplication.presentation.utils.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.enableHorizontalSwipe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {
    private val viewModel: ContactsViewModel by viewModels()
    private var sentId: Long = 0
    private val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter(
            object : ContactsNormalItemListener {
                override fun onBinClick(contact: Contact, position: Int) {
                    deleteRVItem(contact, position)
                }

                override fun onItemClick(
                    contact: Contact, transitionPairs: Array<Pair<View, String>>
                ) {
                    sentId = contact.id
                    navController.navigate(
                        MyContactsFragmentDirections.actionMyContactsToContactsProfile(
                            contact.id
                        ), FragmentNavigatorExtras(*transitionPairs)
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
    }

    override fun onDestroyView() {
        binding.myContactsRvContacts.adapter = null
        super.onDestroyView()
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
            setExitSharedElementCallback(object : androidx.core.app.SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?, sharedElements: MutableMap<String, View>?
                ) {
                    val holder =
                        findViewHolderForAdapterPosition(contactsAdapter.getPositionOfId(sentId))
                    val newSharedElements: MutableMap<String, View>?
                    if (holder is ContactsNormalViewHolder) {
                        newSharedElements = mutableMapOf()
                        holder.transitionPairs.forEach {
                            newSharedElements[it.second] = it.first
                        }
                    } else {
                        newSharedElements = sharedElements
                    }
                    super.onMapSharedElements(names, newSharedElements)
                }
            })
            doOnPreDraw { startPostponedEnterTransition() }
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

    override fun setObservers() {
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

    override fun setListeners() {
        binding.myContactsTextAdd.setOnClickListener {
            navController.navigate(R.id.action_myContacts_to_addContactDialog)
        }
        binding.myContactsImageArrow.setOnClickListener {
            navController.navigateUp()
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
