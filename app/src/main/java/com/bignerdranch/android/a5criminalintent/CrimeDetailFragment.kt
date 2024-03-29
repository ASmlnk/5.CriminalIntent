package com.bignerdranch.android.a5criminalintent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.a5criminalintent.TimePickerFragment.Companion.BUNDLE_KEY_TIME
import com.bignerdranch.android.a5criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeDetailFragment : Fragment() {

    // private lateinit var binding: FragmentCrimeDetailBinding

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {  //получение не нулевого значения _binding
            "не удается получить доступ к binding, поскольку она равна null. Виден ли вид"
        }

    //private lateinit var crime: Crime

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }
    private val suspectIntent = object : ActivityResultContract<Void?, Uri?>() {
        override fun createIntent(context: Context, input: Void?): Intent {
            return Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
        }
    }

    private var photoName: String? = null
    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto ->
        if (didTakePhoto && photoName != null) {
            crimeDetailViewModel.updateCrime { oldCrime ->
                oldCrime.copy(photoFileName = photoName)
            }
        }
    }

    private val selectSuspect = registerForActivityResult(
        suspectIntent         //ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { parseContactSelection(uri) }
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = false,
            isPolice = false
        )
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            crimeTitle.doOnTextChanged { text, _, _, _ ->
                /*crime = crime.copy(title = text.toString())*/
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            /*crimeDate.apply {
                *//*text = crime.date.toString()*//*
                isEnabled = false
            }*/

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                /*crime = crime.copy(isSolved = isChecked)*/
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            crimePolice.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isPolice = isChecked)
                }
            }

            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }

            //для проверки вызова приложения контакты создадим интент (вызываем контакты)
            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)
            crimeCallSuspect.isEnabled = false

            crimeCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.bignerdranch.android.a5criminalintent.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }

            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            crimeCamera.isEnabled = canResolveIntent(captureImageIntent)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.crimeTitle.text.isNullOrBlank()) {
                    Toast.makeText(context, "Нужно ввести название преступления", Toast.LENGTH_LONG)
                        .show()
                } else {
                    NavHostFragment.findNavController(this@CrimeDetailFragment).navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let {
                        updateUi(it)
                    }
                }
            }
        }
        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime {
                it.copy(date = newDate)
            }
        }

        setFragmentResultListener(
            TimePickerFragment.REQUEST_KEY_TIME
        ) { _, bundle ->
            val newTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            crimeDetailViewModel.updateCrime {
                it.copy(date = newTime)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
            crimePolice.isChecked = crime.isPolice
            crimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }
            crimeTime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(crime.date)
                )
            }
            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }
                // startActivity(reportIntent)

                //создание выборщика, выбор будет даже если установлено действие по умолчанию
                val chooserIntent =
                    Intent.createChooser(reportIntent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }  //если не пусто, то будет применено действие по умолчанию в скобках
            /*crimeCallSuspect.text = crime.suspectTel.ifEmpty {
                getString(R.string.crime_call_suspect)
            }*/
            crimeCallSuspect.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:" + crime.suspectTel)
                }
                startActivity(intent)
            }
            crimeCallSuspect.isEnabled = crime.suspectTel.isNotBlank()

            crimePhoto.setOnClickListener {
                val photoFile = crime.photoFileName?.let {
                    File(requireContext().applicationContext.filesDir, it)
                }

                binding.fragmentLayout.doOnLayout { measuredView ->
                    val bitmap = getScaleBitmap(
                        photoFile!!.path,
                        measuredView.width,
                        measuredView.height
                    )
                    findNavController().navigate(
                        CrimeDetailFragmentDirections.selectPhotoCrime(bitmap)
                    )
                }
            }

            crimePhoto.isEnabled = !crime.photoFileName.isNullOrBlank()
        }
        updatePhoto(crime.photoFileName)
    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspectText)
    }

    @SuppressLint("Range")
    private fun parseContactSelection(contactUri: Uri) {
        //запрашиваем все имена отоброжаемых данных(контактов)

        val queryFields = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        //обращаемся к БД контактов и создаем курсор
        //курсор указывает на таблицу БД, содержащую ожну строку и один столбец
        //строка педстовляет контакт, который выбрал пользователь, и указаный столбец имеет имя контакта
        val queryCursor =
            requireActivity().contentResolver.query(
                contactUri, queryFields,
                null, null, null
            )

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                //moveToFirst выполняет перемещение курсора в первую строку, и определяет есть ли данный и возврвщает Boolean
                val suspect = cursor.getString(0)
                // val suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val suspectTel = cursor.getString(1)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect, suspectTel = suspectTel)
                }
            }
        }
    }

    //для проверки может ли наша ОС выполнить неявный интент
    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolvedActivity != null
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.crimePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }

            if (photoFile?.exists() == true) {
                binding.crimePhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaleBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )

                    binding.crimePhoto.setImageBitmap(scaledBitmap)  //binding.crimePhoto.setImageBitmap(scaledBitmap)
                    binding.crimePhoto.tag = photoFileName
                }
            } else {
                binding.crimePhoto.setImageBitmap(null)
                binding.crimePhoto.tag = null
            }
        }
    }
}