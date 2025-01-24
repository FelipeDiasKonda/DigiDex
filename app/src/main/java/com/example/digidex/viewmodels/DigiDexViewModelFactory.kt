package com.example.digidex.viewmodels


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DigiDexViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DigiDexViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DigiDexViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}