package com.example.hiltfirebaselogin_lgomdom.data.repository


import com.example.hiltfirebaselogin_lgomdom.data.remote.ApiService
import com.example.hiltfirebaselogin_lgomdom.models.Personaje
import javax.inject.Inject

class PersonajeRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllPersonajes() = apiService.getAllPersonajes()
    suspend fun createPersonaje(personaje: Personaje) = apiService.createPersonaje(personaje)
    suspend fun updatePersonaje(id: Long, personaje: Personaje) = apiService.updatePersonaje(id, personaje)
    suspend fun deletePersonaje(id: Long) = apiService.deletePersonaje(id)
}
