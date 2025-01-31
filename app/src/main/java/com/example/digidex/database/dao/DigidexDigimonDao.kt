package com.example.digidex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.digidex.database.models.DigidexDigimonModel

@Dao
interface DigidexDigimonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigiDexDigimonCrossRef(crossRef: List<DigidexDigimonModel>)
}