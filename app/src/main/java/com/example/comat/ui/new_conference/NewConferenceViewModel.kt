package com.example.comat.ui.new_conference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewConferenceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new conference Fragment"
    }
    val text: LiveData<String> = _text
}