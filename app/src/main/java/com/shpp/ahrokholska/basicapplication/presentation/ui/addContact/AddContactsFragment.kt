package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentAddContactsBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.adapter.AddContactsAdapter
import com.shpp.ahrokholska.basicapplication.presentation.utils.VerticalSpaceItemDecoration
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.invisible
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment :
    BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate) {
    private val viewModel: AddContactViewModel by viewModels()
    private val addContactsAdapter: AddContactsAdapter by lazy {
        AddContactsAdapter(viewModel::addContact)
    }
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!viewModel.isLoading) {
                navController.navigateUp()
            } else {
                binding.addContactsProgressWindow.visible()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.finishedAll.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                        if (it) {
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun setListeners() {
        binding.addContactsImageArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun setObservers() {
        binding.addContactsProgressBar.visible()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contacts.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { list ->
                list?.let {
                    addContactsAdapter.submitList(list)
                    binding.addContactsProgressBar.invisible()

                    viewModel.states.forEachIndexed { i: Int, stateFlow: StateFlow<State> ->
                        launch {
                            stateFlow.collect {
                                addContactsAdapter.setStateAtPosition(i, it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        binding.addContactsRvContacts.apply {
            adapter = addContactsAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }.addItemDecoration(
            VerticalSpaceItemDecoration(
                resources.getDimension(R.dimen.recycler_item_spacing).toInt()
            )
        )
    }
}