package com.example.digidex.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.digidex.database.dao.DigidexDao
import com.example.digidex.database.dao.DigidexDigimonDao
import com.example.digidex.database.dao.DigimonDao
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.database.models.DigimonModel
import com.example.digidex.database.models.DigidexDigimonModel

@Database(
    entities = [DigidexModel::class, DigimonModel::class, DigidexDigimonModel::class],
    version = 2,
    exportSchema = false
)
abstract class DigiDatabase : RoomDatabase() {
    abstract fun digidexDao(): DigidexDao
    abstract fun digimonDao(): DigimonDao
    abstract fun digidexDigimonDao(): DigidexDigimonDao

    companion object {
        @Volatile
        private var instance: DigiDatabase? = null

        operator fun invoke(context: Context): DigiDatabase = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                DigiDatabase::class.java,
                "digidex_table"
            ).build()
        }.also {
            instance = it
        }
    }
}