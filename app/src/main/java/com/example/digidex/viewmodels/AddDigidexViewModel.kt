package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidex.database.db.DigiDatabase
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class AddDigidexViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DigiRepository

    init {
        val database = DigiDatabase(application)
        repository = DigiRepository(database.digidexDao(), database.digimonDao(), database.digidexDigimonDao())
    }

    fun addDigiDex(digidex: DigidexModel) = viewModelScope.launch {
        repository.insertDigiDex(digidex)
    }
}