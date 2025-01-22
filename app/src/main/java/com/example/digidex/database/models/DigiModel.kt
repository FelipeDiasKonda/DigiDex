package com.example.digidex.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "digi_table")
data class DigiModel (
    @PrimaryKey  val id: Int,
    val name: String,
    val description: String,
    val level: String,
    val attribute: String,
    val type: String
)