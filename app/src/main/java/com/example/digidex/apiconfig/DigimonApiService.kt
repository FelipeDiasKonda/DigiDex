package com.example.digidex.apiconfig

import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.database.models.DigiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigimonApiService {

    @GET("digimon")
    suspend fun getDigimons(
        @Query("name") name: String? =null,
        @Query("exact") exact: Boolean? = null,
        @Query("attribute") attribute: String? = null,
        @Query("xAntibody") xAntibody: Boolean? = null,
        @Query("level") level: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): Response<DigimonsResponse>

    data class DigimonsResponse(
        val content: List<DigiModel>
    )

    @GET("digimon/{id}")
    suspend fun getDigimonById(@Path("id") id: Int): DigiDexModel

    @GET("digimon/{name}")
    suspend fun getDigimonByName(@Path("name") name: String): DigiModel
}