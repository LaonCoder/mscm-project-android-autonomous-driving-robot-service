package com.example.mscmproject.feature_auth.presentation.sign_up

import com.example.mscmproject.feature_auth.domain.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult

data class SignUpState(
    // Google OneTap SignIn Data
    val oneTapSignInResponse: Resource<BeginSignInResult>? = null,
    // Value
    val email: String = "",
    val password: String = "",
    // Status
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signUpError: String? = null
)