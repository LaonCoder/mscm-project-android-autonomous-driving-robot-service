package com.example.mscmproject.navigation

sealed class Screen(val route: String) {
    // Auth
    object Auth : Screen("auth")
    object Splash : Screen("Splash")
    object Login : Screen("login")
    object Register : Screen("register")

    // Home
    object Home : Screen("Home")
    object Main : Screen("Main")
}

