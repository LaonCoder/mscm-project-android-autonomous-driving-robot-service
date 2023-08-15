package com.example.mscmproject.navigation

sealed class Screen(val route: String) {
    // Auth
    object Auth : Screen("Auth")
    object Splash : Screen("Splash")
    object Login : Screen("Login")
    object Register : Screen("Register")

    // Home
    object Home : Screen("Home")
    object Main : Screen("Main")
}

