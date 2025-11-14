package com.example.hiltfirebaselogin_lgomdom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltfirebaselogin_lgomdom.data.repository.AuthRepository
import com.example.hiltfirebaselogin_lgomdom.data.repository.PersonajeRepository
import com.example.hiltfirebaselogin_lgomdom.models.Personaje
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonajeViewModel @Inject constructor(
    private val repository: PersonajeRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _personajes = MutableStateFlow<List<Personaje>>(emptyList())
    val personajes: StateFlow<List<Personaje>> = _personajes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPersonajes()
    }

    fun loadPersonajes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getAllPersonajes()
                if (response.isSuccessful) {
                    _personajes.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }

    // --- Función para cerrar sesión ---
    fun logout() {
        authRepo.logout()
    }

    fun createPersonaje(personaje: Personaje) {
        viewModelScope.launch {
            try {
                repository.createPersonaje(personaje)
                loadPersonajes() // Recarga la lista
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updatePersonaje(id: Long, personaje: Personaje) {
        viewModelScope.launch {
            try {
                repository.updatePersonaje(id, personaje)
                loadPersonajes() // Recarga la lista
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deletePersonaje(id: Long) {
        viewModelScope.launch {
            try {
                repository.deletePersonaje(id)
                loadPersonajes() // Recarga la lista
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
