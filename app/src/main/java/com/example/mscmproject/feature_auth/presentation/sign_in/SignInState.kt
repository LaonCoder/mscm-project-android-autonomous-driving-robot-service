package com.example.mscmproject.feature_auth.presentation.sign_in

data class SignInState(
    // Value
    val email: String = "",
    val password: String = "",
    // Status
    val isLoading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)