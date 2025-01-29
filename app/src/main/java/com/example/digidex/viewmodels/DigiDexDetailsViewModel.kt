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
        val digimonIdsLiveData = repository.getDigimonIdsForDigidex(digidexId)

        digimonIdsLiveData.observeForever { digimonIds ->
            viewModelScope.launch {
                val digimons = repository.getDigimonsByIds(digimonIds)
                _digimons.postValue(digimons)
            }
        }
    }
}