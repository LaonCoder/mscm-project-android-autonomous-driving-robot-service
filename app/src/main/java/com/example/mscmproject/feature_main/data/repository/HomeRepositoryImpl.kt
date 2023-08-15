package com.example.mscmproject.feature_main.data.repository

import com.example.mscmproject.core.Constants.USER
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore
) : HomeRepository {
    override val displayName: String
        get() = firebaseAuth.currentUser?.displayName.toString()
    override val photoUrl: String
        get() = firebaseAuth.currentUser?.photoUrl.toString()

    override suspend fun signOut(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override suspend fun revokeAccess(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            firebaseAuth.currentUser?.apply {
                db.collection(USER).document(uid).delete().await()
                signInClient.revokeAccess().await()
                oneTapClient.signOut().await()
                delete().await()
            }
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}