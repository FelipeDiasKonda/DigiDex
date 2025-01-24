package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.database.models.DigiDexWithDigimons
import com.example.digidex.database.models.DigiModel
import com.example.digidex.database.models.DigidexDigimonModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class DigiDexViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    val allDigiDexes: LiveData<List<DigiDexModel>>
    val allDigimons: LiveData<List<DigiModel>>

    init {
        val digiDexDao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(digiDexDao)
        allDigiDexes = repository.allDigiDexes
        allDigimons = repository.allDigimons
    }

    fun insertDigiDex(digidex: DigiDexModel) = viewModelScope.launch {
        repository.insertDigiDex(digidex)
    }

    fun insertDigimon(digimon: DigiModel) = viewModelScope.launch {
        repository.insertDigimon(digimon)
    }

    fun insertDigiDexDigimonCrossRef(crossRef: DigidexDigimonModel) = viewModelScope.launch {
        repository.insertDigiDexDigimonCrossRef(crossRef)
    }

    fun getDigiDexWithDigimons(digidexId: Int): LiveData<DigiDexWithDigimons> {
        return repository.getDigiDexWithDigimons(digidexId)
    }

    fun getDigimonsExcluding(excludedIds: List<Int>): LiveData<List<DigiModel>> {
        return repository.getDigimonsExcluding(excludedIds)
    }

    fun fetchAndSaveDigimons() = viewModelScope.launch {
        repository.fetchAndSaveDigimons()
    }
}