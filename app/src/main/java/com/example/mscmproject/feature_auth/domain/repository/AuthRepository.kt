package com.example.mscmproject.feature_auth.domain.repository

import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_auth.presentation.sign_in.UserData
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean
    fun getSignedInUser(): UserData?
    fun firebaseSignIn(email: String, password: String): Flow<Resource<AuthResult>>
    fun firebaseSignUp(email: String, password: String): Flow<Resource<AuthResult>>
    fun oneTapSignInWithGoogle(): Flow<Resource<BeginSignInResult>>
    fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<Resource<Boolean>>
    suspend fun signOut()
}