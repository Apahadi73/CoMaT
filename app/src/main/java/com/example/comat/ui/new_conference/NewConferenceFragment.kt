package com.example.comat.ui.new_conference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.comat.R

class NewConferenceFragment : Fragment() {

    private lateinit var newConferenceViewModel: NewConferenceViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newConferenceViewModel =
                ViewModelProvider(this).get(NewConferenceViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_conference, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        newConferenceViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}