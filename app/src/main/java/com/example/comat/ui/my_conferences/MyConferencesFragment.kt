package com.example.comat.ui.my_conferences

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
import com.example.comat.databinding.FragmentMyConferencesBinding
import com.example.comat.databinding.FragmentNewConferenceBinding

class MyConferencesFragment : Fragment() {

    private lateinit var myConferencesViewModel: MyConferencesViewModel
    private lateinit var binding: FragmentMyConferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        myConferencesViewModel =
            ViewModelProvider(this).get(MyConferencesViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_conferences, container, false)
        myConferencesViewModel.fetchUserData()
        val adapter = ConferencesListAdapter()
        binding.recyclerView.adapter = adapter
        myConferencesViewModel.conferenceList.observe(viewLifecycleOwner,{
            it.let{
                it.let {
                    adapter.submitList(it)
                }
            }
        })
        return binding.root
    }
}