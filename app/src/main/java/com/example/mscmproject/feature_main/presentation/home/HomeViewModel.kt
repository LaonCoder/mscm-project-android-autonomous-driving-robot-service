package com.example.mscmproject.feature_main.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _homeUiData = MutableStateFlow(HomeUiData())
    val homeUiData = _homeUiData.asStateFlow()

    val displayName get() = homeRepository.displayName
    val photoUrl get() = homeRepository.photoUrl

    fun getCurrentUser() {
        val currentUser = homeRepository.getCurrentUser()
        if (currentUser != null) {
            _homeUiData.update {
                it.copy(currentUser = currentUser)
            }
        } else {
            Log.d("HomeViewModel/getCurrentUser()", "Cannot fetch Firebase user info.")
        }
    }

    fun userExists() = homeUiData.value.currentUser != null

    fun userEmailExists() = if (userExists()) { homeUiData.value.currentUser!!.email != null } else false

    fun signOut() = viewModelScope.launch {
        homeRepository.signOut().collect { result ->
            when(result) {
                is Resource.Success -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            isSignOutSuccessful = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            signOutError = result.message
                        )
                    }
                }
            }
        }
    }

    fun revokeAccess() = viewModelScope.launch {
        homeRepository.revokeAccess().collect { result ->
            when(result) {
                is Resource.Success -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            isRevokeAccessSuccessful = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = true,
                            revokeAccessError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            revokeAccessError = result.message
                        )
                    }
                }
            }
        }
    }

    fun resetStates() {
        _homeUiState.update { HomeUiState() }
        _homeUiData.update { HomeUiData() }
    }

    fun checkInitialComposition() {
        _homeUiState.update { it.copy(isInitialComposition = false) }
    }

    suspend fun fetchServiceArea() = viewModelScope.launch {
        homeRepository.fetchServiceArea().collect() { result ->
            when(result) {
                is Resource.Success -> {
                    if (!(result.data.isNullOrEmpty())) {
                        Log.d("HomeViewModel/fetchServiceLocations()", result.data.toString())
                        _homeUiData.update {
                            it.copy(
                                serviceAreas = result.data,
                                serviceArea = result.data.find { area -> area.areaCode == 1 }
                            )
                        }
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                isFetchServiceAreaSuccessful = true,
                                fetchServiceAreaError = null
                            )
                        }
                    } else {
                        Log.d("HomeViewModel/fetchServiceLocation()", "Service area data is null or empty")
                        _homeUiState.update {
                            it.copy(
                                fetchServiceAreaError = "Unknown error occurred while retrieving service location data from Firestore."
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = true,
                            fetchServiceAreaError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            fetchServiceAreaError = result.message
                        )
                    }
                }
            }
        }
    }

    fun selectServiceLocation(area: String) {
        val serviceArea = homeUiData.value.serviceAreas.find { it.areaName == area }
        _homeUiData.update {
            it.copy(
                serviceArea = serviceArea,
                gpsPath = null,
                departurePoint = null,
                destinationPoint = null
            )
        }
    }

    fun checkAndUpdateGpsPath() {
        val pathName = homeUiData.value.departurePoint + "@" + homeUiData.value.destinationPoint
        val gpsPath = homeUiData.value.serviceArea!!.gpsPaths.find { it.pathName == pathName }
        if (
            homeUiData.value.serviceArea != null &&
            gpsPath != null
        ) {
            _homeUiData.update { it.copy(gpsPath = gpsPath) }
            _homeUiState.update { it.copy(hasAvailableGpsPath = true) }
        } else {
            _homeUiData.update { it.copy(gpsPath = null) }
            _homeUiState.update { it.copy(hasAvailableGpsPath = false) }
        }
    }

    fun selectDeparturePoint(point: String) {
        if (homeUiData.value.serviceArea!!.servicePoints.find { it.pointName == point } != null) {
            _homeUiData.update {
                it.copy(departurePoint = point)
            }
        } else {
            Log.d("HomeViewModel/selectDestinationPoint()", "Invaild departure point selected.")
        }
    }

    fun selectDestinationPoint(point: String) {
        if (homeUiData.value.serviceArea!!.servicePoints.find { it.pointName == point } != null) {
            _homeUiData.update {
                it.copy(destinationPoint = point)
            }
        } else {
            Log.d("HomeViewModel/selectDeparturePoint()", "Invaild destination point selected.")
        }
    }

    suspend fun dispatchRobot() = viewModelScope.launch {
         if (userEmailExists()) {
            homeRepository.dispatchRobot(
                user = homeUiData.value.currentUser!!.email!!,
                areaName = homeUiData.value.serviceArea!!.areaName,
                departure = homeUiData.value.departurePoint!!,
                destination = homeUiData.value.destinationPoint!!
            ).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                isDispatchRobotSuccessful = true,
                                dispatchRobotError = null
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _homeUiState.update {
                            it.copy(
                                isLoading = true,
                                dispatchRobotError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                dispatchRobotError = result.message
                            )
                        }
                    }
                }
            }
        } else {
            Log.d("HomeViewModel/dispatchRobot()", "Cannot find current user from firebase.")
        }
    }

    fun showServiceLocationDialog(show: Boolean) {
        _homeUiState.update {
            it.copy(showServiceAreaDialog = show)
        }
    }

    fun showHomeBottomSheet(show: Boolean) {
        _homeUiState.update {
            it.copy(showHomeBottomSheet = show)
        }
    }

    fun showDepartureDialog(show: Boolean) {
        _homeUiState.update {
            it.copy(showDepartureDialog = show)
        }
    }

    fun showDestinationDialog(show: Boolean) {
        _homeUiState.update {
            it.copy(showDestinationDialog = show)
        }
    }

    fun showDispatchConfirmationDialog(show: Boolean) {
        _homeUiState.update {
            it.copy(showDispatchConfirmationDialog = show)
        }
    }
}