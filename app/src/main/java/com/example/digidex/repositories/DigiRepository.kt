package com.example.digidex.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.digidex.database.dao.DigiDao
import com.example.digidex.database.models.DigiDexModel
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

    fun getDigimonIdsForDigidex(digidexId: Int): LiveData<List<Int>> {
        return digiDao.getDigiDexWithDigimons(digidexId)
            .map { digiDexWithDigimons ->
                digiDexWithDigimons.digimons.map { it.id }
            }
    }

    suspend fun getDigimonsByIds(digimonIds: List<Int>): List<DigiModel> {
        return withContext(defaultDispatcher) {
            val digimons = digimonIds.mapNotNull { digiDao.getDigimonById(it) }
            Log.d("DIGIMONS_LOADED", "Digimons loaded: $digimons")
            digimons
        }
    }
}