package com.example.comat.ui.conferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConferencesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Conference Fragment"
    }
    val text: LiveData<String> = _text
}