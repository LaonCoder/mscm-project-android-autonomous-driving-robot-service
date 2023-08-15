package com.example.mscmproject.feature_main.presentation.home

import com.example.mscmproject.feature_auth.domain.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult

data class HomeState(
    // Status
    val isLoading: Boolean = false,
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String? = null,
    val isRevokeAccessSuccessful: Boolean = false,
    val revokeAccessError: String? = null
)