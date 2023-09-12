package com.example.mscmproject.feature_main.presentation.home

data class HomeUiState(
    /* Ui */

    // Dialog
    val showServiceAreaDialog: Boolean = false,
    val showDepartureDialog: Boolean = false,
    val showDestinationDialog: Boolean = false,
    val showDispatchConfirmationDialog: Boolean = false,

    // Bottom sheet
    val showHomeBottomSheet: Boolean = false,

    /* Status */

    // Common
    val isLoading: Boolean = false,
    val isInitialComposition: Boolean = true,
    val hasAvailableGpsPath: Boolean = false,

    // Fetch serviceLocation data
    val isFetchServiceAreaSuccessful: Boolean = false,
    val fetchServiceAreaError: String? = null,

    // Dispatch robot
    val isDispatchRobotSuccessful: Boolean = false,
    val dispatchRobotError: String? = null,

    // Sign out
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String? = null,

    // Revoke access
    val isRevokeAccessSuccessful: Boolean = false,
    val revokeAccessError: String? = null
)