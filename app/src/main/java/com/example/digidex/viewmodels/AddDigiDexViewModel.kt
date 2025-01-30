package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class AddDigiDexViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DigiRepository

    init {
        val dao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(dao)
    }

    fun addDigiDex(digidex: DigiDexModel) = viewModelScope.launch {
        repository.insertDigiDex(digidex)
    }
}