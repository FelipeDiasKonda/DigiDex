package com.example.digidex.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["digidexId", "digimonId"],
    foreignKeys = [
        ForeignKey(
            entity = DigiDexModel::class,
            parentColumns = ["id"],
            childColumns = ["digidexId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DigiModel::class,
            parentColumns = ["id"],
            childColumns = ["digimonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["digimonId"])]
)
data class DigidexDigimonModel(
    val digidexId: Int,
    val digimonId: Int
)