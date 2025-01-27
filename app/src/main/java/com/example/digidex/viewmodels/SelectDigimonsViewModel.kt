package com.example.digidex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.digidex.apiconfig.RetrofitInstance
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.launch

class SelectDigimonsViewModel(application: Application) : AndroidViewModel(application) {

    private val _allDigimons = MutableLiveData<List<DigiModel>>()
    val allDigimons: List<DigiModel> get() = _allDigimons.value ?: emptyList()
    private val repository: DigiRepository
    private val _digimons = MutableLiveData<List<DigiModel>>()
    val digimons: LiveData<List<DigiModel>> get() = _digimons

    init{
        val dao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(dao)
    }

    fun fetchDigimons() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDigimons()
                if (response.isSuccessful) {
                    _digimons.value = response.body()?.content
                    Log.d("Digimons", response.body()?.content.toString())
                    Log.d("API_SUCCESS", "Digimons fetched successfully")
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch digimons", e)
            }
        }
    }

    fun addDigimonstoDigidex(digidexId: Int, digimonsIds: List<DigiModel>) {
        viewModelScope.launch {
            try {
                repository.addDigimonsToDigidex(digidexId, digimonsIds)

            } catch (e: Exception) {
                Log.e("DIGIDEX_ERROR", "Response not successful:}")
            }

        }
    }
}
