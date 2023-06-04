package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyContactsBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter.ContactsAdapter
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.SelectionListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsNormalViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.utils.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.enableHorizontalSwipe
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.gone
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.invisible
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.visible
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
                override fun onBinClick(contact: Contact) {
                    deleteRVItem(contact)
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
            },
            object : SelectionListener {
                override fun clearSelection() {
                    viewModel.clearSelectedPositions()
                }

                override fun addItemToSelection(itemId: Long) {
                    viewModel.addSelectedPositionWithId(itemId)
                }

                override fun removeItemFromSelection(itemPos: Int) {
                    viewModel.removeSelectedPosition(itemPos)
                }
            }, ::searchDisabled
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        binding.myContactsRvContacts.adapter = null
        setExitSharedElementCallback(null)
        super.onDestroyView()
    }

    private fun initRecycler() {
        binding.myContactsRvContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
            enableHorizontalSwipe<ContactsNormalViewHolder> {
                val pos = it.adapterPosition
                deleteRVItem(contactsAdapter.currentList[pos])
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
            VerticalSpaceItemDecoration(
                resources.getDimension(R.dimen.recycler_item_spacing).toInt()
            )
        )
    }

    private fun deleteRVItem(contact: Contact) {
        deleteMultipleRVItems(listOf(contact))
    }

    private fun deleteMultipleRVItems(contacts: List<Contact>) {
        contacts.forEach { viewModel.deleteContact(it) }
        Snackbar.make(
            binding.myContactsRvContacts, if (contacts.size == 1) R.string.removed_contact
            else R.string.removed_contacts, RV_TIME_TO_CANCEL_MS
        ).setAction(getString(R.string.undo).uppercase()) {
            contacts.forEach { viewModel.insertContact(it) }
        }.setAnchorView(binding.myContactsRvContacts).show()
    }

    override fun setObservers() {
        binding.contactsProgressBar.visible()
        viewModel.updateContacts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.error.collect {
                        if (it) {
                            binding.contactsErrorWindow.visible()
                        } else {
                            binding.contactsErrorWindow.gone()
                        }
                    }
                }
                launch {
                    viewModel.contacts.collect { list ->
                        list?.let {
                            contactsAdapter.submitList(list)
                            binding.contactsProgressBar.invisible()
                            if (list.isEmpty()) {
                                binding.contactsTextNoContacts.visible()
                            } else {
                                binding.contactsTextNoContacts.gone()
                            }
                        }
                    }
                }
                launch {
                    viewModel.multiselectState.collect {
                        if (it) {
                            binding.myContactsBinBtn.visible()
                        } else {
                            binding.myContactsBinBtn.gone()
                        }
                        searchDisabled(it)
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

    private fun searchDisabled(isDisabled: Boolean) {
        with(binding) {
            if (isDisabled) {
                myContactsSearch.setQuery("", false)
                myContactsSearch.isIconified = true
                enableSearchView(myContactsSearch,false)
            } else {
                myContactsSearch.isEnabled = true
                enableSearchView(myContactsSearch,true)
            }
        }
    }

    override fun setListeners() {
        with(binding) {
            myContactsTextAdd.setOnClickListener {
                navController.navigate(MyContactsFragmentDirections.actionMyContactsToAddContactDialog())
            }
            myContactsImageArrow.setOnClickListener {
                navController.navigateUp()
            }
            myContactsBinBtn.setOnClickListener {
                deleteMultipleRVItems(contactsAdapter.deleteMultiselectItems())
                it.gone()
            }
            contactsTextTryAgain.setOnClickListener {
                viewModel.updateContacts()
            }
        }
        initializeSearchView()
    }

    private fun initializeSearchView() {
        with(binding) {
            myContactsSearch.setOnSearchClickListener {
                myContactsSearch.layoutParams.width = 0
                myContactsTextContacts.invisible()
                myContactsImageArrow.invisible()
            }
            myContactsSearch.setOnCloseListener {
                myContactsSearch.layoutParams.width = WRAP_CONTENT
                viewModel.setFilter(null)
                myContactsTextContacts.visible()
                myContactsImageArrow.visible()
                false
            }
            myContactsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.setFilter(newText)
                    return true
                }
            })
        }
    }

    private fun enableSearchView(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                enableSearchView(child, enabled)
            }
        }
    }

  /*  private fun searchOnClick() {
        with(binding) {
            myContactsSearch.layoutParams.width = 0
            myContactsTextContacts.invisible()
            myContactsImageArrow.invisible()
        }
    }*/

    companion object {
        private const val RV_TIME_TO_CANCEL_MS = 5 * 1000
    }
}
