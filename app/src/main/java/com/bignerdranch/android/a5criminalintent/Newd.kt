package com.bignerdranch.android.a5criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.a5criminalintent.databinding.NewdBinding
import java.util.*

class Newd : Fragment() {
    private var _binding: NewdBinding? = null
    private val binding
        get() = checkNotNull(_binding) {  //получение не нулевого значения _binding
            "не удается получить доступ к binding, поскольку она равна null. Виден ли вид"
        }

    private var x = ""
    private lateinit var crime: Crime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = true,
            isPolice = false
        )

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewdBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ctitle.doOnTextChanged { text, _, _, _ ->
                x = text.toString()

            }

            date.setOnClickListener {
                findNavController().navigate(
                    NewdDirections.actionNewdToDatePickerFragment(crime.date)
                )

            }

            time.setOnClickListener {
                findNavController().navigate(
                    NewdDirections.actionNewdToTimePickerFragment(crime.date)
                )
            }
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (x.isNullOrBlank()) {

                    Toast.makeText(context, "Введи текст", Toast.LENGTH_LONG).show()

                } else NavHostFragment.findNavController(this@Newd).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
        )

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crime = crime.copy(date = newDate)
            updateUi(crime)

        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
            val s = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            crime = crime.copy(date = s)
            updateUi(crime)

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateUi(crime: Crime) {
        binding.apply {
            textDate.text = crime.date.toString()
        }
    }


}


/*
Обеспечение пользовательской обратной навигации путем обработки onBackPressed теперь стало проще благодаря обратным вызовам внутри фрагмента.

class MyFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (true == conditionForCustomAction) {
                    myCustomActionHere()
                } else  NavHostFragment.findNavController(this@MyFragment).navigateUp();
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
        )
        ...
    }
    Если вы хотите, чтобы действие возврата по умолчанию основывалось на каком-либо условии, вы можете использовать:

    NavHostFragment.findNavController(this@MyFragment).navigateUp();*/
