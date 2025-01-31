package com.example.digidex.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.digidex.database.dao.DigidexDao
import com.example.digidex.database.dao.DigimonDao
import com.example.digidex.database.dao.DigidexDigimonDao
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.database.models.DigimonModel
import com.example.digidex.database.models.DigidexDigimonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DigiRepository(
    private val digidexDao: DigidexDao,
    private val digimonDao: DigimonDao,
    private val digidexDigimonDao: DigidexDigimonDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val allDigiDexes: LiveData<List<DigidexModel>> = digidexDao.getAllDigiDexes()

    suspend fun insertDigiDex(digiDexModel: DigidexModel) {
        withContext(defaultDispatcher) {
            digidexDao.insertDigiDex(digiDexModel)
        }
    }

    suspend fun insertDigimon(digiModel: DigimonModel) {
        withContext(defaultDispatcher) {
            digimonDao.insertDigimon(digiModel)
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
            digidexDigimonDao.insertDigiDexDigimonCrossRef(relationship)
        }
    }

    suspend fun digidexExists(digidexId: Int): Boolean {
        return withContext(defaultDispatcher) {
            digidexDao.getDigiDexById(digidexId) != null
        }
    }

    suspend fun getLastDigiDexId(): Int? {
        return withContext(defaultDispatcher) {
            digidexDao.getLastDigiDexId()
        }
    }

    suspend fun digimonExists(digimonId: Int): Boolean {
        return withContext(defaultDispatcher) {
            digimonDao.getDigimonById(digimonId) != null
        }
    }

    fun getDigimonIdsForDigidex(digidexId: Int): LiveData<List<Int>> {
        return digidexDao.getDigiDexWithDigimons(digidexId)
            .map { digiDexWithDigimons ->
                digiDexWithDigimons.digimons.map { it.id }
            }
    }

    suspend fun getDigimonsByIds(digimonIds: List<Int>): List<DigimonModel> {
        return withContext(defaultDispatcher) {
            val digimons = digimonIds.mapNotNull { digimonDao.getDigimonById(it) }
            Log.d("DIGIMONS_LOADED", "Digimons loaded: $digimons")
            digimons
        }
    }

    fun getAllDigimons(): LiveData<List<DigimonModel>> {
        return digimonDao.getAllDigimons()
    }
}