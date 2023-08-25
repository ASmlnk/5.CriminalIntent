package com.bignerdranch.android.a5criminalintent

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimePoliceBinding
import java.util.*

class CrimeHolder(private val binding: ListItemCrimeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        crime: Crime,
        onCrimeClicked: (crimeId: UUID) -> Unit
    ) {  //лямда для вызова функции NavController
        // перехода к следующему фрагменту

        binding.apply {
            crimeTitle.text = crime.title
            crimeDate.text = dateFormat(crime)                        //crime.date.toString()

            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE

            root.setOnClickListener {
                onCrimeClicked(crime.id)
                /*Toast.makeText(
                    binding.root.context,
                    "${crime.title} clicked!",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }
}

class CrimeHolderPolice(private val binding: ListItemCrimePoliceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {

        binding.apply {
            crimeTitle.text = crime.title
            crimeDate.text =
                dateFormat(crime)                                   //crime.date.toString()

            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE

            crimePolice.setOnClickListener {
                onCrimeClicked(crime.id)
                /*Toast.makeText(
                    binding.root.context,
                    "The police went to you",
                    Toast.LENGTH_LONG
                ).show()*/
            }

            root.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "${crime.title} clicked!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listItemCrime = 0
    private val listItemCrimePolice = 1

    override fun getItemViewType(position: Int): Int {
        return if (crimes[position].isPolice) {
            listItemCrimePolice
        } else {
            listItemCrime
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == listItemCrime) {
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            CrimeHolder(binding)
        } else {
            val binding = ListItemCrimePoliceBinding.inflate(inflater, parent, false)
            CrimeHolderPolice(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]

        when (holder) {
            is CrimeHolder -> holder.bind(crime, onCrimeClicked)
            is CrimeHolderPolice -> holder.bind(crime, onCrimeClicked)
        }
    }

    override fun getItemCount() = crimes.size
}

private fun dateFormat(crime: Crime) =
    DateFormat.getPatternInstance(DateFormat.WEEKDAY).format(crime.date) +
            ", " +
            DateFormat.getPatternInstance(DateFormat.DAY).format(crime.date) +
            " " +
            DateFormat.getPatternInstance(DateFormat.MONTH + DateFormat.YEAR).format(crime.date)

