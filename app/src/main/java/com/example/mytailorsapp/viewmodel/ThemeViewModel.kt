package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {

    // Use LiveData for XML-based UI compatibility
    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    // Toggle the theme
    fun toggleTheme() {
        _isDarkTheme.value = _isDarkTheme.value?.not()
    }
}
