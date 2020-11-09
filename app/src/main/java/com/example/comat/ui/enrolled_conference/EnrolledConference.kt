package com.example.comat.ui.enrolled_conference

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.comat.R
import com.example.comat.adapters.ConferenceClickListner
import com.example.comat.adapters.ConferencesListAdapter
import com.example.comat.databinding.EnrolledConferenceFragmentBinding
import com.example.comat.databinding.UpdateConferenceFragmentBinding
import com.example.comat.ui.my_conferences.MyConferencesFragmentDirections

class EnrolledConference : Fragment() {
    private lateinit var binding: EnrolledConferenceFragmentBinding


    companion object {
        fun newInstance() = EnrolledConference()
    }

    private lateinit var viewModel: EnrolledConferenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.enrolled_conference_fragment,
            container,
            false)
        viewModel = ViewModelProvider(this).get(EnrolledConferenceViewModel::class.java)
        viewModel.fetchUserData()
        val adapter = ConferencesListAdapter(ConferenceClickListner { conferenceId ->
            findNavController().navigate(EnrolledConferenceDirections.actionEnrolledConferenceToConference(conferenceId))

        })
        binding.recyclerView.adapter = adapter
        viewModel.conferenceList.observe(viewLifecycleOwner,{
            it.let{
                it.let {
                    adapter.submitList(it)
                }
            }
        })
        return binding.root
    }

}