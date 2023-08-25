package com.bignerdranch.android.a5criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import com.bignerdranch.android.a5criminalintent.databinding.NewdBinding

class Newd : Fragment() {
    private var _binding: NewdBinding? = null
    private val binding
        get() = checkNotNull(_binding) {  //получение не нулевого значения _binding
            "не удается получить доступ к binding, поскольку она равна null. Виден ли вид"
        }

    private var x = ""

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
        binding.ctitle.doOnTextChanged { text, _, _, _ ->
            x = text.toString()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
