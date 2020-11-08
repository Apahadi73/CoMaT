package com.example.comat.ui.ui_shared

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.comat.R
import com.example.comat.databinding.ConferencePageFragmentBinding

class ConferencePage : Fragment() {
    private lateinit var binding: ConferencePageFragmentBinding

    companion object {
        fun newInstance() = ConferencePage()
    }

    private lateinit var pageViewModel: ConferencePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.conference_page_fragment, container, false)
        val conferenceId = arguments?.let{ ConferencePageArgs.fromBundle(it).conferenceId}
        binding.conferenceId.text = conferenceId
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(ConferencePageViewModel::class.java)
    }

}