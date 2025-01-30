package com.example.digidex.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectDigimonsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DigiRepository
    private val _digimons = MutableLiveData<List<DigiModel>>()
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    val digimons: LiveData<List<DigiModel>> get() = _digimons
    private val _digimonDetails = MutableLiveData<DigiModel?>()
    val digimonDetails: LiveData<DigiModel?> get() = _digimonDetails
    private val _finishActivity = MutableLiveData<Boolean>()
    val finishActivity: LiveData<Boolean> get() = _finishActivity

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
                    val digimonsList = response.body()?.content ?: emptyList()
                    repository.getAllDigimons().observeForever { existingDigimons ->
                        val filteredDigimons = digimonsList.filter { digimon ->
                            existingDigimons.none { it.id == digimon.id }
                        }
                        _digimons.value = filteredDigimons
                        Log.d("Digimons", filteredDigimons.toString())
                        Log.d("API_SUCCESS", "Digimons fetched successfully")
                    }
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
                    val saveJobs = digimons.map { digimonId ->
                        async { saveDigimon(digimonId, digidexId) }
                    }
                    saveJobs.awaitAll() // Espera todas as operações de salvamento concluírem

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplication<Application>().applicationContext,
                            "Digimons added to digidex",
                            Toast.LENGTH_SHORT
                        ).show()
                        _finishActivity.value = true
                    }
                } else {
                    Log.e("DIGIDEX_ERROR", "Invalid DigiDex ID: $digidexId")
                }
            } catch (e: Exception) {
                Log.e("DIGIDEX_ERROR", "Response not successful:", e)
            }
        }
    }


    private suspend fun saveDigimon(digimon: Int, digidexId: Int) {
        val detailedDigimon = fetchDigimonDetails(digimon)
        if (detailedDigimon != null) {
            val imagePath = saveImageLocally(detailedDigimon.image)
            val digimonModel = detailedDigimon.copy(
                image = imagePath,
                description = detailedDigimon.description,
                level = detailedDigimon.level,
                attribute = detailedDigimon.attribute,
                fields = detailedDigimon.fields,
                type = detailedDigimon.type
            )
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
        return try {
            val response = RetrofitInstance.api.getDigimonDetails(id)
            if (response.isSuccessful) {
                val digimonDetail = response.body()

                digimonDetail?.let {
                    DigiModel(
                        id = it.id,
                        name = it.name,
                        description = it.descriptions.firstOrNull { desc -> desc.language == "en_us" }?.description
                            ?: "No description available",
                        level = it.levels.firstOrNull()?.level ?: "Unknown",
                        attribute = it.attributes.firstOrNull()?.attribute ?: "Unknown",
                        type = it.types.firstOrNull()?.type ?: "Unknown",
                        fields = it.fields.firstOrNull()?.field ?: "Unknown",
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
