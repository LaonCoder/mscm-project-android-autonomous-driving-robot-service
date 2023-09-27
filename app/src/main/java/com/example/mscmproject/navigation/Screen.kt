package com.example.mscmproject.navigation

sealed class Screen(val route: String) {
    // Auth
    object Auth : Screen("Auth")
    object Splash : Screen("Splash")
    object Login : Screen("Login")
    object Register : Screen("Register")

    // Main
    object Main : Screen("Main")
    object Home : Screen("Home")
}

