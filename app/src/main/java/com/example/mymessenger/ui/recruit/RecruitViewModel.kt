package com.example.mymessenger.ui.recruit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecruitViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is recruit Fragment"
    }
    val text: LiveData<String> = _text
}