package com.example.digidex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digidex.database.models.DigimonModel

@Dao
interface DigimonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigimon(digimon: DigimonModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigimons(digimons: List<DigimonModel>)

    @Query("SELECT * FROM digi_table")
    fun getAllDigimons(): LiveData<List<DigimonModel>>

    @Query("SELECT * FROM digi_table WHERE id = :digimonId")
    suspend fun getDigimonById(digimonId: Int): DigimonModel?

    @Query("SELECT * FROM digi_table WHERE id NOT IN (:excludedIds)")
    fun getDigimonsExcluding(excludedIds: List<Int>): LiveData<List<DigimonModel>>
}