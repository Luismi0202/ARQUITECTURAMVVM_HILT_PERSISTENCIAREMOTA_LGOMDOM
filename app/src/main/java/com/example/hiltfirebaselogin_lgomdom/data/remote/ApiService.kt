package com.example.hiltfirebaselogin_lgomdom.data.remote

import com.example.hiltfirebaselogin_lgomdom.models.Habilidad
import com.example.hiltfirebaselogin_lgomdom.models.Personaje
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/personajes")
    suspend fun getAllPersonajes(): Response<List<Personaje>>

    @GET("api/personajes/{id}")
    suspend fun getPersonajeById(@Path("id") id: Long): Response<Personaje>

    @POST("api/personajes")
    suspend fun createPersonaje(@Body personaje: Personaje): Response<Personaje>

    @PUT("api/personajes/{id}")
    suspend fun updatePersonaje(@Path("id") id: Long, @Body personaje: Personaje): Response<Personaje>

    @DELETE("api/personajes/{id}")
    suspend fun deletePersonaje(@Path("id") id: Long): Response<Unit>

    @GET("api/habilidades") // corregido para incluir /api/
    suspend fun getAllHabilidades(): Response<List<Habilidad>>
}
