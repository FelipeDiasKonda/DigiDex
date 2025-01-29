package com.example.digidex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class DigiDexDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    private val _digimons = MutableLiveData<List<DigiModel>>()
    val digimons: LiveData<List<DigiModel>> get() = _digimons

    init {
        val dao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(dao)
    }

    fun loadDigimons(digidexId: Int) {
        viewModelScope.launch {
            try {
                val digimonIds = repository.getDigimonIdsForDigidex(digidexId)
                Log.d("DIGIMON_IDS", "Digimon IDs: $digimonIds")
                val digimons = repository.getDigimonsByIds(digimonIds)
                Log.d("DIGIMONS", "Digimons: $digimons")
                _digimons.postValue(digimons)
            } catch (e: Exception) {
                Log.e("LOAD_DIGIMONS_ERROR", "Error loading digimons", e)
                _digimons.postValue(emptyList())
            }
        }
    }
}