package com.example.comat.ui.conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.comat.R
import com.example.comat.adapters.ConferencesListAdapter
import com.example.comat.databinding.FragmentConferencesBinding

class ConferencesFragment : Fragment() {

    private lateinit var conferencesViewModel: ConferencesViewModel
    private lateinit var binding: FragmentConferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        conferencesViewModel =
            ViewModelProvider(this).get(ConferencesViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conferences, container, false)
        conferencesViewModel.fetchUserData()
        val adapter = ConferencesListAdapter()
        binding.recyclerView.adapter = adapter
        conferencesViewModel.conferenceList.observe(viewLifecycleOwner, {
            it.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }
}