package com.example.mscmproject.feature_main.domain.repository
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_auth.presentation.sign_in.SignInState
import com.example.mscmproject.feature_main.presentation.home.HomeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface HomeRepository {
    val displayName: String
    val photoUrl: String

    suspend fun signOut(): Flow<Resource<Boolean>>
    suspend fun revokeAccess(): Flow<Resource<Boolean>>
}