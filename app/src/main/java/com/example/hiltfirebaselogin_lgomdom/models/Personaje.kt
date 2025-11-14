package com.example.hiltfirebaselogin_lgomdom.models

data class Personaje(
    val id: Long? = null,
    val nombre: String = "",
    val tripulacion: String = "",
    val habilidades: List<Habilidad> = emptyList()
)
