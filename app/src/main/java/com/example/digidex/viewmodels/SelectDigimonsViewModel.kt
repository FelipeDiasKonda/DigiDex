package com.example.digidex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidex.apiconfig.RetrofitInstance
import com.example.digidex.database.models.DigiModel
import kotlinx.coroutines.launch

class SelectDigimonsViewModel : ViewModel() {

    private val _digimons = MutableLiveData<List<DigiModel>>()
    val digimons: LiveData<List<DigiModel>> get() = _digimons

    fun fetchDigimons() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDigimons()
                if (response.isSuccessful) {
                    val digimons = response.body()?.content
                    Log.d("Digimons", digimons.toString())
                    Log.d("API_SUCCESS", "Digimons fetched successfully")
                    // Aqui você pode usar os dados conforme necessário, sem armazená-los
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch digimons", e)
            }
        }


    }
    fun selectDigimon(digimon: DigiModel) {}

    fun completeSelection() {}
}