package com.bignerdranch.android.a5criminalintent

import android.icu.text.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimePoliceBinding
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimePoliceSwipeBinding
import com.bignerdranch.android.a5criminalintent.databinding.ListItemCrimeSwipeBinding
import com.daimajia.swipe.SwipeLayout
import java.util.*

class CrimeHolderSwipe(private val binding: ListItemCrimeSwipeBinding) :     ///class CrimeHolderSwipe(private val view: View) :
    RecyclerView.ViewHolder(binding.root) {

    //private val binding = ListItemCrimeSwipeBinding.bind(view)



    fun bind(
        crime: Crime,
        onCrimeClicked: (crimeId: UUID) -> Unit,
        onCrimeDeleteClicked: (crime: Crime, position: Int) -> Unit
    ) {  //лямда для вызова функции NavController
        // перехода к следующему фрагменту

        binding.listItemCrimeSwipe.apply {
            showMode = SwipeLayout.ShowMode.PullOut                //SwipeLayout.ShowMode.LayDown

            addDrag(SwipeLayout.DragEdge.Right, binding.swipeButton)
            addDrag(SwipeLayout.DragEdge.Left, null)

            addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout?) {
                    Log.i("111111", "onStartOpen")
                }

                override fun onOpen(layout: SwipeLayout?) {
                    Log.i("111111", "onOpen")
                    binding.linearLayout.isClickable = false
                    binding.deleteButton.setOnClickListener {
                        onCrimeDeleteClicked(crime, absoluteAdapterPosition)
                    }
                }

                override fun onStartClose(layout: SwipeLayout?) {
                    Log.i("111111", "onStartClose")
                }

                override fun onClose(layout: SwipeLayout?) {
                    Log.i("111111", "onClose")
                    binding.linearLayout.isClickable = true
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
                    Log.i("111111", "onUpdate")
                }

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                    Log.i("111111", "onHandRelease")
                }
            })
        }

        binding.apply {
            crimeTitle.text = crime.title
            crimeTitle.text = crime.title
            crimeDate.text = dateFormat(crime)                        //crime.date.toString()
            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            linearLayout.setOnClickListener {
                onCrimeClicked(crime.id)
                /*root.setOnClickListener {

                    Toast.makeText(
                        binding.root.context,
                        "${crime.title} clicked!",
                        Toast.LENGTH_SHORT
                    ).show()*/
            }
        }
    }
}

class CrimeHolderSwipePolice(private val binding: ListItemCrimePoliceSwipeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        crime: Crime,
        onCrimeClicked: (crimeId: UUID) -> Unit,
        onCrimeDeleteClicked: (crime: Crime, position: Int) -> Unit
    ) {

        binding.listItemCrimeSwipe.apply {
            showMode = SwipeLayout.ShowMode.PullOut                //SwipeLayout.ShowMode.LayDown

            addDrag(SwipeLayout.DragEdge.Right, binding.swipeButton)
            addDrag(SwipeLayout.DragEdge.Left, null)

            addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout?) {
                }

                override fun onOpen(layout: SwipeLayout?) {
                    binding.linearLayout.isClickable = false
                    binding.deleteButton.setOnClickListener {
                        onCrimeDeleteClicked(crime, absoluteAdapterPosition)
                    }
                }

                override fun onStartClose(layout: SwipeLayout?) {
                }

                override fun onClose(layout: SwipeLayout?) {
                    binding.linearLayout.isClickable = true
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
                }

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                }
            })
        }

        binding.apply {
            crimeTitle.text = crime.title
            crimeTitle.text = crime.title
            crimeDate.text = dateFormat(crime)
            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            linearLayout.setOnClickListener {
                onCrimeClicked(crime.id)
            }
            crimePolice.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "The police went to you",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


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
                // onCrimeClicked(crime.id)
                Toast.makeText(
                    binding.root.context,
                    "The police went to you",
                    Toast.LENGTH_LONG
                ).show()
            }

            root.setOnClickListener {
                onCrimeClicked(crime.id)
            }

        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit,
    private val onCrimeDeleteClicked: (crime: Crime, position: Int) -> Unit
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
        // val s = LayoutInflater.from(parent.context)
        //     .inflate(R.layout.list_item_crime_swipe, parent, false)

        return if (viewType == listItemCrime) {
            val binding = ListItemCrimeSwipeBinding.inflate(inflater, parent, false)
            CrimeHolderSwipe(binding)
        } else {
            val binding = ListItemCrimePoliceSwipeBinding.inflate(inflater, parent, false)
            CrimeHolderSwipePolice(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]

        when (holder) {
            is CrimeHolderSwipe -> holder.bind(
                crime,
                onCrimeClicked,
                onCrimeDeleteClicked
            )
            is CrimeHolderSwipePolice -> holder.bind(
                crime,
                onCrimeClicked,
                onCrimeDeleteClicked
            )
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

interface OnNotifyItemChanged {
    fun onNotifyItemAdapter(position: Int)
}

