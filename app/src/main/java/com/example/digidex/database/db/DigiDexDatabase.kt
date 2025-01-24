package com.example.digidex.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.digidex.database.Dao.DigiDao
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.database.models.DigiModel
import com.example.digidex.database.models.DigidexDigimonModel

@Database(entities = [DigiDexModel::class, DigiModel::class, DigidexDigimonModel::class], version = 1, exportSchema = false)
abstract class DigiDexDatabase : RoomDatabase() {
    abstract fun digiDexDao(): DigiDao

    companion object {
        @Volatile
        private var instance: DigiDexDatabase? = null

        operator fun invoke(context: Context): DigiDexDatabase = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                DigiDexDatabase::class.java,
                "digidex_table"
            ).build()
        }.also {
            instance = it
        }
    }
}