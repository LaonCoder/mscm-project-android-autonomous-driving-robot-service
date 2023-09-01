package com.example.mscmproject.feature_main.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
    }


    // ------------------------------test-----------------------------------

    fun checkInitialComposition() {
        _homeUiState.update { it.copy(isInitialComposition = false) }
    }

    suspend fun fetchServiceLocation() = viewModelScope.launch {
        homeRepository.fetchServiceLocation().collect() { result ->
            when(result) {
                is Resource.Success -> {
                    if (!(result.data.isNullOrEmpty())) {
                        Log.d("HomeViewModel/fetchServiceLocation()", result.data.toString())
                        _homeUiData.update {
                            it.copy(
                                serviceLocations = result.data,
                                serviceLocation = result.data.find { location -> location.areaCode == 1 }
                            )
                        }
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                isFetchServiceLocationSuccessful = true,
                                fetchServiceLocationError = null
                            )
                        }
                    } else {
                        Log.d("HomeViewModel/fetchServiceLocation()", "Service Location data is null or empty")
                        _homeUiState.update {
                            it.copy(
                                fetchServiceLocationError = "Unknown error occurred while retrieving service location data from Firestore."
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = true,
                            fetchServiceLocationError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            fetchServiceLocationError = result.message
                        )
                    }
                }
            }
        }
        Log.d("HomeViewModel/fetchServiceLocation()", "Function ended.")
    }

    fun selectServiceLocation(location: String) {
        val serviceLocation = homeUiData.value.serviceLocations.find { it.location == location }
        _homeUiData.update {
            it.copy(serviceLocation = serviceLocation)
        }
    }

    fun selectDeparturePoint(point: String) {
        if (homeUiData.value.serviceLocation!!.points.contains(point)) {
            _homeUiData.update {
                it.copy(departureLocation = point)
            }
        } else {
            Log.d("HomeViewModel/selectDestinationPoint()", "Invaild departure point selected.")
        }
    }

    fun selectDestinationPoint(point: String) {
        if (homeUiData.value.serviceLocation!!.points.contains(point)) {
            _homeUiData.update {
                it.copy(destinationLocation = point)
            }
        } else {
            Log.d("HomeViewModel/selectDeparturePoint()", "Invaild destination point selected.")
        }
    }

    fun showServiceLocationDialog(show: Boolean) {
        _homeUiState.update {
            it.copy(showServiceLocationDialog = show)
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

}