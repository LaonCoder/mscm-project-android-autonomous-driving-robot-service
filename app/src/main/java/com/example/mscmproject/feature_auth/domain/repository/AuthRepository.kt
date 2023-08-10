package com.example.mscmproject.feature_auth.domain.repository

import com.example.mscmproject.feature_auth.domain.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun register(email: String, password: String): Flow<Resource<AuthResult>>

}