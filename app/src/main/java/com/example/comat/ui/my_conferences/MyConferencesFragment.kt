package com.example.comat.ui.my_conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.comat.R

class MyConferencesFragment : Fragment() {

    private lateinit var myConferencesViewModel: MyConferencesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        myConferencesViewModel =
                ViewModelProvider(this).get(MyConferencesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_conferences, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        myConferencesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }
}