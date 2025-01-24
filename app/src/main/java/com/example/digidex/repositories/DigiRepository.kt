package com.example.digidex.repositories

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

    suspend fun insertDigiDexDigimonCrossRef(crossRef: DigidexDigimonModel) {
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
        val digimons = RetrofitInstance.api.getDigimons()
        digimons.forEach { digiDao.insertDigimon(it) }
    }

}