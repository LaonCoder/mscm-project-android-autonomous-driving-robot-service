package com.example.mscmproject.feature_main.domain.repository
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.model.ServiceLocation
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val displayName: String
    val photoUrl: String

    suspend fun signOut(): Flow<Resource<Boolean>>
    suspend fun revokeAccess(): Flow<Resource<Boolean>>
    suspend fun fetchServiceLocation(): Flow<Resource<ArrayList<ServiceLocation>>>
}