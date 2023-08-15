package com.example.mscmproject.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInScreen
import com.example.mscmproject.feature_auth.presentation.sign_up.SignUpScreen
import com.example.mscmproject.feature_auth.presentation.splash.SplashScreen
import com.example.mscmproject.feature_main.presentation.home.HomeScreen

@Composable
fun NavGraph(
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
                    navController = navController,
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
                HomeScreen(
                    navController = navController,
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

