package com.example.digidex.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "digidex_table")
data class DigiDexModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String?
)