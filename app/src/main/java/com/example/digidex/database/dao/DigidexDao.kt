package com.example.digidex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.database.models.DigidexWithDigimons

@Dao
interface DigidexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigiDex(digidex: DigidexModel): Long

    @Query("SELECT * FROM digidex_table")
    fun getAllDigiDexes(): LiveData<List<DigidexModel>>

    @Delete
    suspend fun deleteDigiDex(digidex: DigidexModel)

    @Query("SELECT * FROM digidex_table WHERE id = :digidexId LIMIT 1")
    suspend fun getDigiDexById(digidexId: Int): DigidexModel?

    @Transaction
    @Query("SELECT * FROM digidex_table WHERE id = :digidexId")
    fun getDigiDexWithDigimons(digidexId: Int): LiveData<DigidexWithDigimons>

    @Query("SELECT MAX(id) FROM digidex_table")
    suspend fun getLastDigiDexId(): Int?
}