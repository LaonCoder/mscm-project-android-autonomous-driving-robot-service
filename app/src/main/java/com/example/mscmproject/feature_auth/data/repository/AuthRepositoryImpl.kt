package com.example.mscmproject.feature_auth.data.repository

import android.util.Log
import com.example.mscmproject.core.Constants.CREATED_AT
import com.example.mscmproject.core.Constants.DISPLAY_NAME
import com.example.mscmproject.core.Constants.EMAIL
import com.example.mscmproject.core.Constants.PHOTO_URL
import com.example.mscmproject.core.Constants.SIGN_IN_REQUEST
import com.example.mscmproject.core.Constants.SIGN_UP_REQUEST
import com.example.mscmproject.core.Constants.USER
import com.example.mscmproject.feature_auth.domain.repository.AuthRepository
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_auth.presentation.sign_in.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val isUserAuthenticatedInFirebase = firebaseAuth.currentUser != null

    override fun getSignedInUser(): UserData? = firebaseAuth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun firebaseSignUp(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun oneTapSignInWithGoogle(): Flow<Resource<BeginSignInResult>> {
        return flow {
            emit(Resource.Loading())
            val result = oneTapClient.beginSignIn(signInRequest).await()
            emit(Resource.Success(result))
        }.catch {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Resource.Success(signUpResult))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<Resource<Boolean>> {
        return flow {
            Log.d("Google Login", "firebaseSigninWithGoogle")
            emit(Resource.Loading())
            val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Log.d("Google Login", "firebaseSigninWithGoogle3")
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    private suspend fun addUserToFirestore() {
        firebaseAuth.currentUser?.apply {
            val user = toUser()
            db.collection(USER).document(uid).set(user).await()
        }
    }

    private fun FirebaseUser.toUser() = mapOf(
        DISPLAY_NAME to displayName,
        EMAIL to email,
        PHOTO_URL to photoUrl?.toString(),
        CREATED_AT to serverTimestamp()
    )

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
}