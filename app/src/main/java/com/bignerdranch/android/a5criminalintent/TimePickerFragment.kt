package com.bignerdranch.android.a5criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = Date()


        val timeListener = TimePickerDialog.OnTimeSetListener {_, hour: Int, minute: Int ->
            val resultTime = GregorianCalendar()

        }
        return TimePickerDialog(
            requireContext(),
            null,

        )
    }

}