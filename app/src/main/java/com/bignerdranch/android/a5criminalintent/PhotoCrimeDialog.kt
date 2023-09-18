package com.bignerdranch.android.a5criminalintent

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.a5criminalintent.databinding.PhotoDialogCrimeBinding

class PhotoCrimeDialog : DialogFragment() {

    private var _binding: PhotoDialogCrimeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
        }

    private val args: PhotoCrimeDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = PhotoDialogCrimeBinding.inflate(LayoutInflater.from(context))
        val photoBitmap = args.bitmapCrime
        binding.photoCrimeDialog.setImageBitmap(photoBitmap)
        val dialog = AlertDialog.Builder(requireActivity()).create()
        dialog.apply {
            setView(binding.root)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}