// Kotlin
package com.example.hiltfirebaselogin_lgomdom.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            // Log completo para ver stacktrace en Logcat
            Log.e("AuthRepository", "login error", e)
            val message = if (e is FirebaseAuthException) {
                // Devuelve el error code de Firebase (p. ej. ERROR_WRONG_PASSWORD)
                e.errorCode ?: e.message
            } else {
                e.message
            }
            Result.failure(Exception(message ?: "Error de autenticación desconocido"))
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Log.e("AuthRepository", "register error", e)
            val message = if (e is FirebaseAuthException) {
                e.errorCode ?: e.message
            } else {
                e.message
            }
            Result.failure(Exception(message ?: "Error de registro desconocido"))
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
