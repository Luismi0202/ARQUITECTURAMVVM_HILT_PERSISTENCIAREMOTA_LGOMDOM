package com.example.hiltfirebaselogin_lgomdom.models

import com.google.gson.annotations.SerializedName

data class Habilidad(
    val id: Long? = null,
    val nombre: String = "",
    val descripcion: String = "",
    @SerializedName("personaje_id")
    val personajeId: Long? = null // opcional: si el backend no lo devuelve, seguirá funcionando
)
