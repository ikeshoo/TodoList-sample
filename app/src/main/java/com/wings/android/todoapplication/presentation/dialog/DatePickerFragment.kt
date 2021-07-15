package com.wings.android.todoapplication.presentation.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.wings.android.todoapplication.R
import java.util.*

class DatePickerFragment:DialogFragment(),DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(),this,year,month,day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //setFragmentResult("fromDate", bundleOf("year" to year,"month" to month,"day" to dayOfMonth))
        val editText = requireActivity().findViewById<EditText>(R.id.et_deadline)
        editText.setText(String.format("%d-%02d-%02d",year,month+1,dayOfMonth))
    }

}