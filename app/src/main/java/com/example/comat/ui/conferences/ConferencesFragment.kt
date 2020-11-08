package com.example.comat.ui.conferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.comat.R
import com.example.comat.adapters.ConferenceClickListner
import com.example.comat.adapters.ConferencesListAdapter
import com.example.comat.databinding.FragmentConferencesBinding
import com.example.comat.ui.ui_shared.ConferencePage

class ConferencesFragment : androidx.fragment.app.Fragment() {

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
        val adapter = ConferencesListAdapter(ConferenceClickListner { conferenceId ->
            Log.d("conferenceId",conferenceId)
            findNavController().navigate(ConferencesFragmentDirections.actionNavConferencesToConference(conferenceId))
        })
        binding.recyclerView.adapter = adapter
        conferencesViewModel.conferenceList.observe(viewLifecycleOwner, {
            it.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }
}