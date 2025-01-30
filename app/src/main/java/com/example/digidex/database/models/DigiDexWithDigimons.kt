package com.example.digidex.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DigiDexWithDigimons(
    @Embedded val digidex: DigiDexModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DigidexDigimonModel::class,
            parentColumn = "digidexId",
            entityColumn = "digimonId"
        )
    )
    val digimons: List<DigiModel>
)
