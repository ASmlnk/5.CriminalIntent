package com.bignerdranch.android.a5criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.a5criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

   // private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vrt.setOnClickListener {
            findNavController().navigate(R.id.action_crimeListFragment_to_newd)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes) { crimeid ->
                        findNavController().navigate(
                            /*R.id.show_crime_detail*/
                        CrimeListFragmentDirections.showCrimeDetail(crimeid)
                        )     //лямда для вызова функции NavController
                                                                                // перехода к следующему фрагменту
                    }

                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
