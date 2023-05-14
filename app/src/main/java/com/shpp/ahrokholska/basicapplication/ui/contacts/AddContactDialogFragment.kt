package com.shpp.ahrokholska.basicapplication.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.shpp.ahrokholska.basicapplication.databinding.AddContactDialogBinding

class AddContactDialogFragment(private val onSave: (name: String, career: String) -> Unit) :
    DialogFragment() {
    private var _binding: AddContactDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            addContactDialogBtnSave.setOnClickListener {
                onSave(
                    addContactDialogEtName.text.toString(), addContactDialogEdCareer.text.toString()
                )
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "com.shpp.ahrokholska.basicapplication.AddContactDialogFragment"
    }
}