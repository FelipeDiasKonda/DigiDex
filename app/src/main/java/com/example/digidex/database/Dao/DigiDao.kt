package com.example.digidex.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Query
import androidx.room.Update
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.database.models.DigiDexWithDigimons
import com.example.digidex.database.models.DigiModel
import com.example.digidex.database.models.DigidexDigimonModel

@Dao
interface DigiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigiDex(digidex: DigiDexModel): Long

    @Query("SELECT * FROM digidex_table")
    fun getAllDigiDexes(): LiveData<List<DigiDexModel>>

    @Delete
    suspend fun deleteDigiDex(digidex: DigiDexModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigimon(digimon: DigiModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigimons(digimons: List<DigiModel>)

    @Query("SELECT * FROM digi_table")
    fun getAllDigimons(): LiveData<List<DigiModel>>

    @Query("SELECT * FROM digi_table WHERE id = :digimonId")
    suspend fun getDigimonById(digimonId: Int): DigiModel?

    @Query("SELECT * FROM digidex_table WHERE id = :digidexId LIMIT 1")
    suspend fun getDigiDexById(digidexId: Int): DigiDexModel?

    @Query("SELECT * FROM digi_table WHERE id NOT IN (:excludedIds) LIMIT 1")
    fun getDigimonsExcluding(excludedIds: List<Int>): LiveData<List<DigiModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigiDexDigimonCrossRef(crossRef: List<DigidexDigimonModel>)

    @Transaction
    @Query("SELECT * FROM digidex_table WHERE id = :digidexId")
    fun getDigiDexWithDigimons(digidexId: Int): LiveData<DigiDexWithDigimons>


    @Update
    suspend fun update(digimon: DigiModel)

    @Query("SELECT MAX(id) FROM digidex_table")
    suspend fun getLastDigiDexId(): Int?

    @Query("SELECT * FROM digidex_table WHERE id = :digidexId LIMIT 1")
    suspend fun getDigidexById(digidexId: Int): DigiDexModel?
}