package com.example.hiltfirebaselogin_lgomdom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hiltfirebaselogin_lgomdom.ui.screens.LoginScreen
import com.example.hiltfirebaselogin_lgomdom.ui.screens.PersonajesScreen
import com.example.hiltfirebaselogin_lgomdom.ui.screens.RegisterScreen
import com.example.hiltfirebaselogin_lgomdom.ui.theme.HiltFirebaseLogin_LgomdomTheme
import com.example.hiltfirebaselogin_lgomdom.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiltFirebaseLogin_LgomdomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Kotlin
@Composable
fun AppNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val startDestination = viewModel.getStartDestination()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("personajes") {
            PersonajesScreen(navController = navController)
        }
    }
}