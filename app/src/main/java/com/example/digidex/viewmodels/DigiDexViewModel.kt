package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.repositories.DigiRepository


class DigiDexViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    val allDigiDexes: LiveData<List<DigiDexModel>>

    init {
        val digiDexDao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(digiDexDao)
        allDigiDexes = repository.allDigiDexes
    }

}