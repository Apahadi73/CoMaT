package com.example.comat.ui.ui_shared

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.comat.R
import com.example.comat.adapters.ScheduleAdapter
import com.example.comat.databinding.ConferencePageFragmentBinding
import com.example.comat.ui.new_conference.NewConferenceViewModel
import com.squareup.picasso.Picasso

class ConferencePage : Fragment() {
    private lateinit var binding: ConferencePageFragmentBinding
    private lateinit var conferencePageViewModel: ConferencePageViewModel

    companion object {
        fun newInstance() = ConferencePage()
    }

    private lateinit var pageViewModel: ConferencePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.conference_page_fragment, container, false)
        val conferenceId = arguments?.let { ConferencePageArgs.fromBundle(it).conferenceId }
        conferencePageViewModel = ViewModelProvider(this).get(ConferencePageViewModel::class.java)
        if (conferenceId != null) {
            conferencePageViewModel.fetchConferenceData(conferenceId)
            Log.d("conference", conferencePageViewModel.conference.toString())
            conferencePageViewModel.conference.observe(viewLifecycleOwner, {
                if (it.logoUrl != "") {
                    Picasso.get().load(it.logoUrl).into(binding.logoView)
                }
                Log.d("item",it.toString())
                binding.conference = it
                val adapter = ScheduleAdapter(requireContext())
                adapter.fillSchedule(it.schedule)
                binding.scheduleListView.adapter = adapter
            })
        }
        binding.enrollBtn.setOnClickListener {
            if (conferenceId != null) {
                conferencePageViewModel.enroll(conferenceId)
            }
        }
        conferencePageViewModel.isEnrolled.observe(viewLifecycleOwner,{
            if(it==true){
                findNavController().navigate(R.id.action_conference_to_nav_conferences)
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(ConferencePageViewModel::class.java)
    }

}