package com.example.hiltfirebaselogin_lgomdom.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hiltfirebaselogin_lgomdom.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun getStartDestination(): String {
        return if (authRepository.currentUser != null) "personajes" else "login"
    }
}
