package com.example.digidex.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.digidex.apiconfig.RetrofitInstance
import com.example.digidex.database.Dao.DigiDao
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.database.models.DigiDexWithDigimons
import com.example.digidex.database.models.DigiModel
import com.example.digidex.database.models.DigidexDigimonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DigiRepository(
    private val digiDao: DigiDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val allDigiDexes: LiveData<List<DigiDexModel>> = digiDao.getAllDigiDexes()
    val allDigimons: LiveData<List<DigiModel>> = digiDao.getAllDigimons()

    suspend fun insertDigiDex(digiDexModel: DigiDexModel) {
        withContext(defaultDispatcher) {
            digiDao.insertDigiDex(digiDexModel)
        }
    }

    suspend fun insertDigimon(digiModel: DigiModel) {
        withContext(defaultDispatcher) {
            digiDao.insertDigimon(digiModel)
        }
    }

    suspend fun insertDigiDexDigimonCrossRef(crossRef: List<DigidexDigimonModel>) {
        withContext(defaultDispatcher) {
            digiDao.insertDigiDexDigimonCrossRef(crossRef)
        }
    }

    fun getDigiDexWithDigimons(digidexId: Int): LiveData<DigiDexWithDigimons> {
        return digiDao.getDigiDexWithDigimons(digidexId)
    }

    fun getDigimonsExcluding(excludedIds: List<Int>): LiveData<List<DigiModel>> {
        return digiDao.getDigimonsExcluding(excludedIds)
    }

    suspend fun fetchAndSaveDigimons() {
        try {
            val response = RetrofitInstance.api.getDigimons()
            if (response.isSuccessful) {
                val digimons = response.body()?.content
                digimons?.forEach { digiDao.insertDigimon(it) }
            } else {
                Log.e("API_ERROR", "Response not successful: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to fetch and save digimons", e)
        }
    }

    suspend fun addDigimonsToDigidex(digidexId: Int, digimons: List<DigiModel>) {
        withContext(defaultDispatcher) {
            val existingDigimons = digiDao.getDigimonsId()
            val newDigimons = digimons.filter { digimon -> digimon.id !in existingDigimons }

            if (newDigimons.isNotEmpty()) {
                digiDao.insertDigimons(newDigimons)
            }
            val relationship = digimons.map { digimon ->
                DigidexDigimonModel(
                    digidexId = digidexId,
                    digimonId = digimon.id
                )
            }
            digiDao.insertDigiDexDigimonCrossRef(relationship)
        }
    }

}