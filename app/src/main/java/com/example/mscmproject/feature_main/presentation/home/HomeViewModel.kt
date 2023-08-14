package com.example.mscmproject.feature_main.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInState
import com.example.mscmproject.feature_auth.presentation.sign_up.SignUpState
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
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    val displayName get() = homeRepository.displayName
    val photoUrl get() = homeRepository.photoUrl

    fun signOut() = viewModelScope.launch {
        homeRepository.signOut().collect { result ->
            when(result) {
                is Resource.Success -> {
                    _homeState.update {
                        it.copy(
                            isLoading = false,
                            isSignOutSuccessful = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _homeState.update {
                        it.copy(
                            isLoading = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeState.update {
                        it.copy(
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
                    _homeState.update {
                        it.copy(
                            isLoading = false,
                            isRevokeAccessSuccessful = true,
                            signOutError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _homeState.update {
                        it.copy(
                            isLoading = true,
                            revokeAccessError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _homeState.update {
                        it.copy(
                            revokeAccessError = result.message
                        )
                    }
                }
            }
        }
    }

    fun resetStates() {
        _homeState.update { HomeState() }
    }
}