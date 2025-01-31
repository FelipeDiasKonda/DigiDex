package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.digidex.database.db.DigiDatabase
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.repositories.DigiRepository

class DigidexViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    val allDigiDexes: LiveData<List<DigidexModel>>

    init {
        val database = DigiDatabase(application)
        repository = DigiRepository(database.digidexDao(), database.digimonDao(), database.digidexDigimonDao())
        allDigiDexes = repository.allDigiDexes
    }
}
