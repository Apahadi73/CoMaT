package com.example.comat.ui.my_conferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyConferencesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my conferences Fragment"
    }
    val text: LiveData<String> = _text
}