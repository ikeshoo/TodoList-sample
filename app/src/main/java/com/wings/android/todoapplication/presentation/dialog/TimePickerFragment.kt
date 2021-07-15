package com.wings.android.todoapplication.presentation.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import android.text.format.DateFormat
import android.widget.Switch
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.wings.android.todoapplication.R
import java.util.*

class TimePickerFragment :DialogFragment(),TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(),this,hour,minute,DateFormat.is24HourFormat(requireContext()))
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val switch = requireActivity().findViewById<Switch>(R.id.switch_notification)
        switch.isChecked = false
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        setFragmentResult("fromTime", bundleOf("hour" to hourOfDay,"minute" to minute))
    }
}