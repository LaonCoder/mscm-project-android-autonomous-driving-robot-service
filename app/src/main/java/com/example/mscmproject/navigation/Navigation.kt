package com.example.mscmproject.navigation

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.mscmproject.feature_auth.presentation.AuthViewModel
import com.example.mscmproject.feature_auth.presentation.sign_in.GoogleAuthUiClient
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInScreen
import com.example.mscmproject.feature_auth.presentation.sign_up.SignUpScreen
import com.example.mscmproject.feature_auth.presentation.splash.SplashScreen
import com.example.mscmproject.feature_main.presentation.home.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    googleAuthUiClient: GoogleAuthUiClient,
    activityScope: LifecycleCoroutineScope,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route)
    {
        navigation(
            startDestination = Screen.Splash.route,
            route = Screen.Auth.route
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    navController = navController
                )
            }
            composable(Screen.Login.route) {
                SignInScreen(
                    googleAuthUiClient = googleAuthUiClient,
                    navController = navController,
                    activityScope = activityScope
                )
            }
            composable(Screen.Register.route) {
                SignUpScreen(
                    navController = navController,
                )
            }
        }
        navigation(
            startDestination = Screen.Home.route,
            route = Screen.Main.route
        ) {
            composable(Screen.Home.route) {
                val applicationContext = LocalContext.current.applicationContext
                MainScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        activityScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Main.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
//    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
//    val parentEntry = remember(this) {
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return hiltViewModel(parentEntry)
//}

