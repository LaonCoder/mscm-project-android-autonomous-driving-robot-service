package com.example.mscmproject.feature_auth.presentation.sign_in

import com.example.mscmproject.feature_auth.domain.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult

data class SignInState(
    // Google OneTap SignIn Data
    val oneTapSignInResponse: Resource<BeginSignInResult>? = null,
    // Value
    val email: String = "",
    val password: String = "",
    // Status
    val isLoading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)