package com.shpp.ahrokholska.basicapplication.presentation.ui.editProfile

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.DATE_REQUEST_KEY

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthStr = if (month < 10) "0$month" else month
        val dayOfMonthStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
        setFragmentResult(
            DATE_REQUEST_KEY, bundleOf(DATE_REQUEST_KEY to "$year-$monthStr-$dayOfMonthStr")
        )
    }
}