package com.example.digidex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.digidex.database.db.DigiDatabase
import com.example.digidex.database.models.DigimonModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class DigidexDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    private val _digimons = MutableLiveData<List<DigimonModel>>()
    val digimons: LiveData<List<DigimonModel>> get() = _digimons

    init {
        val database = DigiDatabase(application)
        repository = DigiRepository(database.digidexDao(), database.digimonDao(), database.digidexDigimonDao())
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