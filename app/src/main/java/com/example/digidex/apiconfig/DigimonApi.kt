package com.example.digidex.apiconfig

import com.example.digidex.database.models.DigimonModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigimonApi {

    @GET("digimon")
    suspend fun getDigimons(
        @Query("name") name: String? =null,
        @Query("exact") exact: Boolean? = null,
        @Query("attribute") attribute: String? = null,
        @Query("xAntibody") xAntibody: Boolean? = null,
        @Query("level") level: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = 1460
    ): Response<DigimonsResponse>

    data class DigimonsResponse(
        val content: List<DigimonModel>
    )

    @GET("digimon/{id}")
    suspend fun getDigimonDetails(
        @Path("id") id: Int
    ): Response<DigimonDetailResponse>

    data class DigimonDetailResponse(
        val id: Int,
        val name: String,
        val xAntibody: Boolean,
        val images: List<Image>,
        val levels: List<Level>,
        val types: List<Type>,
        val attributes: List<Attribute>,
        val fields: List<Field>,
        val releaseDate: String,
        val descriptions: List<Description>,
        val skills: List<Skill>,
        val priorEvolutions: List<Evolution>,
        val nextEvolutions: List<Evolution>
    )

    data class Image(val href: String, val transparent: Boolean)
    data class Level(val id: Int, val level: String)
    data class Type(val id: Int, val type: String)
    data class Attribute(val id: Int, val attribute: String)
    data class Field(val id: Int, val field: String, val image: String)
    data class Description(val origin: String, val language: String, val description: String)
    data class Skill(val id: Int, val skill: String, val translation: String, val description: String)
    data class Evolution(val id: Int, val digimon: String, val condition: String, val image: String, val url: String)
}
