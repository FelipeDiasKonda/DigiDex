package com.example.digidex.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "digi_table")
data class DigiModel (
    @PrimaryKey  val id: Int,
    val name: String,
    var description: String? = "No description available",
    var level: String? = "Unknown",
    var attribute: String? = "Unknown",
    var type: String? = "Unknown",
    var image:String
)