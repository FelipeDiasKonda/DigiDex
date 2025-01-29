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

    suspend fun addDigimonsToDigidex(digidexId: Int, digimons: List<Int>) {
        withContext(defaultDispatcher) {
            val relationship = digimons.map { digimon ->
                DigidexDigimonModel(
                    digidexId = digidexId,
                    digimonId = digimon
                )
            }
            Log.d("DIGIDEX_INSERT", "Inserting relationship: $relationship")
            digiDao.insertDigiDexDigimonCrossRef(relationship)
        }
    }

    suspend fun digidexExists(digidexId: Int): Boolean {
        return withContext(defaultDispatcher) {
            digiDao.getDigiDexById(digidexId) != null
        }
    }

    suspend fun getLastDigiDexId(): Int? {
        return withContext(defaultDispatcher) {
            digiDao.getLastDigiDexId()
        }
    }
    suspend fun digimonExists(digimonId: Int): Boolean {
        return withContext(defaultDispatcher) {
            digiDao.getDigimonById(digimonId) != null
        }
    }
}