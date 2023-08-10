package com.example.mscmproject.feature_auth.presentation.sign_up

data class SignUpState(
    // Value
    val email: String = "",
    val password: String = "",
    // Status
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signUpError: String? = null
)