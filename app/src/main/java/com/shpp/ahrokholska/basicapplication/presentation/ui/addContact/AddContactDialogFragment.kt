package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.shpp.ahrokholska.basicapplication.databinding.AddContactDialogBinding

class AddContactDialogFragment : DialogFragment() {
    private var _binding: AddContactDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            addContactDialogBtnSave.setOnClickListener {
                viewModel.addContact(
                    addContactDialogEtName.text.toString(),
                    addContactDialogEtCareer.text.toString()
                )
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}