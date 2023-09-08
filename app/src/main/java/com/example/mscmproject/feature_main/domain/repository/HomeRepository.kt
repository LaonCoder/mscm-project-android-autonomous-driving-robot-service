package com.example.mscmproject.feature_main.domain.repository
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.model.GpsPath
import com.example.mscmproject.feature_main.domain.model.ServiceArea
import com.example.mscmproject.feature_main.domain.model.ServicePoint
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val displayName: String
    val photoUrl: String

    fun getCurrentUser(): FirebaseUser?
    suspend fun signOut(): Flow<Resource<Boolean>>
    suspend fun revokeAccess(): Flow<Resource<Boolean>>
    suspend fun fetchServiceArea(): Flow<Resource<ArrayList<ServiceArea>>>
    suspend fun callAvailableRobot(): Flow<Resource<ArrayList<ServiceArea>>>
}