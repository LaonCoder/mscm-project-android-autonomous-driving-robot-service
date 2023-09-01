package com.example.mscmproject.feature_main.presentation.home

data class HomeUiState(
    /* Ui */

    // Dialog
    val showServiceLocationDialog: Boolean = false,
    val showDepartureDialog: Boolean = false,
    val showDestinationDialog: Boolean = false,

    // Bottom sheet
    val showHomeBottomSheet: Boolean = false,

    /* Status */

    // Common
    val isLoading: Boolean = false,
    val isInitialComposition: Boolean = true,

    // Fetch serviceLocation data
    val isFetchServiceLocationSuccessful: Boolean = false,
    val fetchServiceLocationError: String? = null,

    // Sign out
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String? = null,

    // Revoke access
    val isRevokeAccessSuccessful: Boolean = false,
    val revokeAccessError: String? = null
)