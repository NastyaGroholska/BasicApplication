package com.shpp.ahrokholska.basicapplication.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ActivityMyContactsBinding
import com.shpp.ahrokholska.basicapplication.ui.shared.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.utils.ext.enableHorizontalSwipe
import com.shpp.ahrokholska.basicapplication.utils.ext.getHeight
import kotlinx.coroutines.launch

class MyContacts : AppCompatActivity() {
    private val binding: ActivityMyContactsBinding by lazy {
        ActivityMyContactsBinding.inflate(layoutInflater)
    }
    private val viewModel: ContactsViewModel by viewModels()
    private val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter(onBinClick = ::deleteRVItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
        setObservers()
        setListeners()
    }

    private fun initRecycler() {
        binding.myContactsRvContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(this@MyContacts)
            enableHorizontalSwipe {
                val pos = it.adapterPosition
                deleteRVItem(contactsAdapter.currentList[pos], pos)
            }
        }.addItemDecoration(
            VerticalSpaceItemDecoration((getHeight() * RV_ITEM_SPACE_PERCENT).toInt())
        )
    }

    private fun deleteRVItem(contact: Contact, position: Int) {
        viewModel.deleteContact(contact)
        Snackbar.make(binding.myContactsRvContacts, R.string.removed_contact, RV_TIME_TO_CANCEL)
            .setAction(getString(R.string.undo).uppercase()) {
                viewModel.insertContact(contact, position)
            }.setAnchorView(binding.myContactsRvContacts).show()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { list ->
                    contactsAdapter.submitList(list)
                }
            }
        }
    }

    private fun setListeners(){
        binding.myContactsTextAdd.setOnClickListener {
            AddContactDialogFragment(viewModel::addContact)
                .show(supportFragmentManager, AddContactDialogFragment.TAG)
        }
    }

    companion object {
        private const val RV_ITEM_SPACE_PERCENT = 0.02
        private const val RV_TIME_TO_CANCEL = 5 * 1000
    }
}