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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectDigimonsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    private val _digimons = MutableLiveData<List<DigiModel>>()
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    val digimons: LiveData<List<DigiModel>> get() = _digimons
    private val _digimonDetails = MutableLiveData<DigiModel?>()
    val digimonDetails: LiveData<DigiModel?> get() = _digimonDetails

    init {
        val dao = DigiDexDatabase(application).digiDexDao()
        repository = DigiRepository(dao)
    }

    fun fetchDigimons(level: String? = null, attribute: String? = null) {
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.api.getDigimons(level = level, attribute = attribute)
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

    fun addDigimonstoDigidex(digidexId: Int, digimons: List<Int>) {
        viewModelScope.launch {
            try {
                if (repository.digidexExists(digidexId)) {
                    digimons.forEach { digimonId ->
                        saveDigimon(digimonId, digidexId)
                    }
                    Log.d("DIGIMONS", "Digimons: $digimons")
                } else {
                    Log.e("DIGIDEX_ERROR", "Invalid DigiDex ID: $digidexId")
                }
            } catch (e: Exception) {
                Log.e("DIGIDEX_ERROR", "Response not successful:", e)
            }
        }
    }


    private fun saveDigimon(digimon: Int, digidexId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailedDigimon = fetchDigimonDetails(digimon)
            Log.d("DETAILED_DIGIMON", "detaild $detailedDigimon")
            if (detailedDigimon != null) {
                val imagePath = saveImageLocally(detailedDigimon.image)
                val digimonModel = detailedDigimon.copy(
                    image = imagePath,
                    description = detailedDigimon.description,
                    level = detailedDigimon.level,
                    attribute = detailedDigimon.attribute,
                    type = detailedDigimon.type
                )
                Log.d("DIGIMON_SAVE", "Saving digimon: $digimonModel")
                repository.insertDigimon(digimonModel)
                if (repository.digimonExists(digimonModel.id)) {
                    repository.addDigimonsToDigidex(digidexId, listOf(digimonModel.id))
                } else {
                    Log.e("DIGIMON_SAVE", "Invalid Digimon ID: ${digimonModel.id}")
                }
            } else {
                Log.e("DIGIMON_SAVE", "Failed to fetch details for Digimon ID: $digimon")
            }
        }
    }


    private suspend fun saveImageLocally(imageUrl: String): String {
        return withContext(defaultDispatcher) {
            val futureTarget = Glide.with(getApplication<Application>().applicationContext)
                .asFile()
                .load(imageUrl)
                .submit()

            val file = futureTarget.get()
            file.absolutePath
        }
    }

    private suspend fun fetchDigimonDetails(id: Int): DigiModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getDigimonDetails(id)
                if (response.isSuccessful) {
                    val digimonDetail = response.body()
                    Log.d("API_RESPONSE", "Fetched details for Digimon ID: $id - $digimonDetail")

                    digimonDetail?.let {
                        DigiModel(
                            id = it.id,
                            name = it.name,
                            description = it.descriptions.firstOrNull { desc -> desc.language == "en_us" }?.description
                                ?: "No description available",
                            level = it.levels.firstOrNull()?.level ?: "Unknown",
                            attribute = it.attributes.firstOrNull()?.attribute ?: "Unknown",
                            type = it.types.firstOrNull()?.type ?: "Unknown",
                            image = it.images.firstOrNull()?.href ?: ""
                        )
                    }
                } else {
                    Log.e(
                        "API_ERROR",
                        "Failed to fetch details for Digimon ID: $id - ${
                            response.errorBody()?.string()
                        }"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception while fetching details for Digimon ID: $id", e)
                null
            }
        }
    }

    fun getLastDigiDexId(): LiveData<Int> {
        val lastDigiDexId = MutableLiveData<Int>()
        viewModelScope.launch {
            val id = repository.getLastDigiDexId()
            lastDigiDexId.postValue(id ?: -1)
        }
        return lastDigiDexId
    }
    fun fetchDigimonDetailsLiveData(id: Int) {
        viewModelScope.launch {
            val digimon = fetchDigimonDetails(id)
            _digimonDetails.postValue(digimon)
        }
    }

}
