package com.bignerdranch.android.a5criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = GregorianCalendar.getInstance()

        val timeListener = TimePickerDialog.OnTimeSetListener { _, hour: Int, minute: Int ->
            calendar.time = args.crimeDate
            val resultTime = calendar
            resultTime.set(Calendar.HOUR, hour)
            resultTime.set(Calendar.MINUTE, minute)
            val resul = resultTime.time
            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to resul))

        }



        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)



        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour,
            minute,
            true
        )
    }

    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }

}