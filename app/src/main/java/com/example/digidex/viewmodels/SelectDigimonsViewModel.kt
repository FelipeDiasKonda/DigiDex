package com.example.digidex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.digidex.apiconfig.RetrofitInstance
import com.example.digidex.database.db.DigiDexDatabase
import com.example.digidex.database.models.DigiModel
import com.example.digidex.repositories.DigiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun fetchDigimons(level: String? = null, attribute: String? = null) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDigimons(level = level, attribute = attribute)
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

    fun addDigimonstoDigidex(digidexId: Int, digimons: List<DigiModel>) {
        viewModelScope.launch {
            try {
                digimons.forEach { digimon ->
                    saveDigimon(digimon)
                }
                repository.addDigimonsToDigidex(digidexId, digimons)
            } catch (e: Exception) {
                Log.e("DIGIDEX_ERROR", "Response not successful:}", e)
            }
        }
    }
    private fun saveDigimon(digimon: DigiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val imagePath = saveImageLocally(digimon.image)
            val digimonModel = digimon.copy(
                image = imagePath,
                description = digimon.description ?: "No description available",
                level = digimon.level ?: "Unknown",
                attribute = digimon.attribute ?: "Unknown",
                type = digimon.type ?: "Unknown"
            )
            repository.insertDigimon(digimonModel)
        }
    }
    private suspend fun saveImageLocally(imageUrl: String): String {
        return withContext(Dispatchers.IO) {
            val futureTarget = Glide.with(getApplication<Application>().applicationContext)
                .asFile()
                .load(imageUrl)
                .submit()

            val file = futureTarget.get()
            file.absolutePath
        }
    }
}
