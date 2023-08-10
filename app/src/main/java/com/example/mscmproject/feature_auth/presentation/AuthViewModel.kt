package com.example.mscmproject.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mscmproject.feature_auth.domain.repository.AuthRepository
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInResult
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInState
import com.example.mscmproject.feature_auth.presentation.sign_up.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    // login state
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    // register state
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        authRepository.loginUser(email, password).collect { result ->
            when(result) {
                is Resource.Success -> {
                    _signInState.update {
                        it.copy(
                            isLoading = false,
                            isSignInSuccessful = result.data != null,
                            signInError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _signInState.update {
                        it.copy(
                            isLoading = true,
                            signInError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _signInState.update {
                        it.copy(
                            signInError = result.message
                        )
                    }
                }
            }
        }
    }


    fun registerUser(email: String, password: String) = viewModelScope.launch {
        authRepository.register(email, password).collect { result ->
            when(result) {
                is Resource.Success -> {
                    _signUpState.update {
                        it.copy(
                            isLoading = false,
                            isSignUpSuccessful = result.data != null,
                            signUpError = null
                        )
                    }
                }
                is Resource.Loading -> {
                    _signUpState.update {
                        it.copy(
                            isLoading = true,
                            signUpError = null
                        )
                    }
                }
                is Resource.Error -> {
                    _signUpState.update {
                        it.copy(
                            signUpError = result.message
                        )
                    }
                }
            }
        }
    }

    fun onSignInEmailChanged(email: String) {
        _signInState.update { it.copy(email = email) }
    }

    fun onSignInPasswordChanged(password: String) {
        _signInState.update { it.copy(password = password) }
    }

    fun onSignUpEmailChanged(email: String) {
        _signUpState.update { it.copy(email = email) }
    }

    fun onSignUpPasswordChanged(password: String) {
        _signUpState.update { it.copy(password = password) }
    }

    fun onSignInResult(result: SignInResult) {
        _signInState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetStates() {
        _signInState.update { SignInState() }
        _signUpState.update { SignUpState() }
    }
}
