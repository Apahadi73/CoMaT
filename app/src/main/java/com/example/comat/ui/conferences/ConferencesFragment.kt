package com.example.comat.ui.conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.comat.R

class ConferencesFragment : Fragment() {

    private lateinit var conferencesViewModel: ConferencesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        conferencesViewModel =
                ViewModelProvider(this).get(ConferencesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_conferences, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        conferencesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}