// Kotlin
package com.example.hiltfirebaselogin_lgomdom.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltfirebaselogin_lgomdom.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        // Validaciones rápidas antes de llamar a Firebase
        if (trimmedEmail.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Email y contraseña son obligatorios")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _loginState.value = LoginState.Error("Formato de email inválido")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.login(trimmedEmail, password)
                .onSuccess { _loginState.value = LoginState.Success }
                .onFailure {
                    val raw = it.message ?: "Error desconocido"
                    val friendly = when {
                        raw.contains("ERROR_INVALID_EMAIL", true) || raw.contains("malformed", true) -> "Email inválido"
                        raw.contains("ERROR_WRONG_PASSWORD", true) || raw.contains("wrong password", true) -> "Contraseña incorrecta"
                        raw.contains("ERROR_USER_NOT_FOUND", true) || raw.contains("no user", true) -> "Usuario no encontrado"
                        raw.contains("ERROR_USER_DISABLED", true) || raw.contains("disabled", true) -> "Cuenta deshabilitada"
                        raw.contains("expired", true) || raw.contains("EXPIRED", true) -> "Credenciales expiradas. Vuelve a iniciar sesión"
                        else -> raw
                    }
                    _loginState.value = LoginState.Error(friendly)
                }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.register(email.trim(), password)
                .onSuccess { _loginState.value = LoginState.Success }
                .onFailure { _loginState.value = LoginState.Error(it.message ?: "Error desconocido") }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
