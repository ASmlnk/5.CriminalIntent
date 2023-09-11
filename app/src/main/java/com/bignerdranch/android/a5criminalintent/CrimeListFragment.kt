package com.bignerdranch.android.a5criminalintent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.a5criminalintent.Swipe.SwipeHelper
import com.bignerdranch.android.a5criminalintent.Swipe.SwipeHelper2k
import com.bignerdranch.android.a5criminalintent.databinding.FragmentCrimeListBinding
import com.daimajia.swipe.SwipeLayout
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()
   // private lateinit var itemTouchHelper: ItemTouchHelper
    private val onNotifyItemChanged = object : OnNotifyItemChanged {
        override fun onNotifyItemAdapter(position: Int) {
            //binding.crimeRecyclerView.adapter?.notifyItemChanged(position)
            binding.crimeRecyclerView.findViewHolderForAdapterPosition(position)
                ?.itemView?.findViewById<SwipeLayout>(R.id.list_item_crime_swipe_lay)?.close()
        }
    }

    // private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val a = activity as AppCompatActivity
        val aa = a.supportActionBar
        aa?.setTitle(R.string.new_crime)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        //binding.crimeRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCrime.setOnClickListener {
            showNewCrime()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes,onNotifyItemChanged, { crimeId ->
                        findNavController().navigate(
                            /*R.id.show_crime_detail*/
                            CrimeListFragmentDirections.showCrimeDetail(crimeId)
                        )     //лямда для вызова функции NavController
                        // перехода к следующему фрагменту
                    }) { crime, position ->
                        crimeListViewModel.deleteCrime(crime)
                        //binding.crimeRecyclerView.adapter?.notifyItemChanged(position)
                    }
                }
            }
        }
        /* val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.crimeRecyclerView) {
             @SuppressLint("ResourceType")
             override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {

                 val deleteButton = deleteButton(position)
                 val buttons = listOf<UnderlayButton>(deleteButton)
                 return buttons
             }
         })
         itemTouchHelper.attachToRecyclerView(binding.crimeRecyclerView)*/

       // binding.crimeRecyclerView.itemAnimator?.endAnimations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                showNewCrime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(requireContext(),
            "Delete",
            14.0f,
            R.color.recycler_view_background,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    //Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                    Toast.makeText(requireContext(), "Crime delete", Toast.LENGTH_LONG).show()
                    val crimes = crimeListViewModel.crimes.value
                    val crime = crimes[position]
                    binding.crimeRecyclerView.adapter?.notifyItemRemoved(position)
                    crimeListViewModel.deleteCrime(crime)
                }
            })
    }

    private fun showNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false,
                isPolice = false
            )

            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(newCrime.id)
            )
        }
    }
}

/*........ fun onCreateView......
...................
    *//* val crimes = crimeListViewModel.crimes
    * val adapter = CrimeListAdapter(crimes)
    * binding.crimeRecyclerView.adapter = adapter*//*

    return binding.root
}*/


/*
*запуск и остановка карутины

override fun onStart() {
    super.onStart()

    job = viewLifecycleOwner.lifecycleScope.launch {
        val crimes = crimeListViewModel.loadCrimes()
        binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes)
    }
}

override fun onStop() {
    super.onStop()
    job?.cancel()
}*/


/*                          Activity
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        })
    }
}
                                Fragment
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    // The usage of an interface lets you inject your own implementation
    val menuHost: MenuHost = requireActivity()

    menuHost.addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            // Add menu items here
            menuInflater.inflate(R.menu.main_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            // Handle the menu selection
            return when (menuItem.itemId) {
                R.id.action_menu1 -> {
                    // todo menu1
                    true
                }
                R.id.action_menu2 -> {
                    // todo menu2
                    true
                }
                else -> false
            }
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}
                            PreferenceFragmentCompat
val menuHost: MenuHost = requireHost() as MenuHost
//Same declaration with Fragment

                            Use MenuProvider interface
class FirstFragment : Fragment(), MenuProvider {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Add menu items here
        menuInflater.inflate(R.menu.second_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Handle the menu selection
        return when (menuItem.itemId) {
            R.id.menu_clear -> {
                // Do stuff...
                true
            }
            R.id.menu_refresh -> {
                // Do stuff...
                true
            }
            else -> false
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}*/
