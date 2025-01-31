package com.example.digidex.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DigidexWithDigimons(
    @Embedded val digidex: DigidexModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DigidexDigimonModel::class,
            parentColumn = "digidexId",
            entityColumn = "digimonId"
        )
    )
    val digimons: List<DigimonModel>
)
