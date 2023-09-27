package com.example.mscmproject.feature_main.data.repository

import android.util.Log
import com.example.mscmproject.core.Constants.USER
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.domain.model.GpsPath
import com.example.mscmproject.feature_main.domain.model.InvalidDispatchException
import com.example.mscmproject.feature_main.domain.model.InvalidGpsPathException
import com.example.mscmproject.feature_main.domain.model.InvalidServiceAreaException
import com.example.mscmproject.feature_main.domain.model.InvalidServicePointException
import com.example.mscmproject.feature_main.domain.model.ServiceArea
import com.example.mscmproject.feature_main.domain.model.ServicePoint
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
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

    override fun getCurrentUser() = firebaseAuth.currentUser

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


    override suspend fun fetchServiceArea(): Flow<Resource<ArrayList<ServiceArea>>> {
        val serviceAreaArray: ArrayList<ServiceArea> = arrayListOf()
        val servicePointArray: ArrayList<ServicePoint> = arrayListOf()
        val gpsPathArray: ArrayList<GpsPath> = arrayListOf()

        return flow {
            emit(Resource.Loading())
            db.collection("serviceArea")
                .apply {
                    this.get().addOnSuccessListener { areas ->
                        for (area in areas) {
                            Log.d("HomeRepositoryImpl/fetchServiceArea()", "${area.id} => ${area.data}")

                            // Fetch sub collection ["points"]
                            this.document(area.id)
                                .collection("points")
                                .get()
                                .addOnSuccessListener { points ->
                                    for (point in points) {
                                        servicePointArray.add(
                                            ServicePoint(
                                                pointName = point["pointName"] as String,
                                                description = point["description"] as String,
                                                coordinate = point["coordinate"] as GeoPoint,
                                                photoUrl = point["photoUrl"] as String
                                            )
                                        )
                                    }
                                }
                                .addOnFailureListener { e ->
                                    throw InvalidServicePointException(
                                        e.message ?: "Cannot fetch servicePoint data from Firestore."
                                    )
                                }

                            // Fetch sub collection ["paths"]
                            this.document(area.id)
                                .collection("paths")
                                .get()
                                .addOnSuccessListener { paths ->
                                    for (path in paths) {
                                        gpsPathArray.add(
                                            GpsPath(
                                                pathName = path["pathName"] as String,
                                                track = path["track"] as ArrayList<GeoPoint>
                                            )
                                        )
                                    }
                                }
                                .addOnFailureListener { e ->
                                    throw InvalidGpsPathException(
                                        e.message ?: "Cannot fetch gpsPath data from Firestore."
                                    )
                                }

                            serviceAreaArray.add(
                                ServiceArea(
                                    areaCode = (area.data["areaCode"] as Long).toInt(),
                                    areaName = area.data["areaName"] as String,
                                    defaultCoordinate = area.data["defaultCoordinate"] as GeoPoint,
                                    defaultCameraZoomScale = (area.data["defaultCameraZoomScale"] as Double).toFloat(),
                                    servicePoints = servicePointArray,
                                    gpsPaths = gpsPathArray
                                )
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        throw InvalidServiceAreaException(
                            e.message ?: "Cannot fetch serviceArea data from Firestore."
                        )
                    }
                    .await()
                    emit(Resource.Success(serviceAreaArray))
                }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override suspend fun dispatchRobot(
        user: String,
        areaName: String,
        departure: String,
        destination: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val dispatchDocRef = db.collection("dispatch")
                .document(areaName)
                .collection("robots")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("status", "IDLE")
                .limit(1)
                .get()
                .await()
                .documents.first().reference

            db.runTransaction { transaction ->
                val snapshot = transaction.get(dispatchDocRef)
                val status = snapshot.getString("status")
                val currentTimestamp = Timestamp(System.currentTimeMillis() / 1000, 0)

                when (status) {
                    "IDLE" -> {
                        transaction
                            .update(
                                dispatchDocRef, mapOf(
                                    "user" to user,
                                    "departure" to departure,
                                    "destination" to destination,
                                    "status" to "RESERVED",
                                    "timestamp" to currentTimestamp
                                )
                            )
                    }
                    // in case "RESERVED"
                    else -> {
                        throw FirebaseFirestoreException(
                            "There are no available robots at the moment.",
                            FirebaseFirestoreException.Code.ABORTED,
                        )
                    }
                }
            }.addOnSuccessListener { result ->
                Log.d("HomeRepositoryImpl/dispatchRobot()", "Transaction success: $result.")
            }.addOnFailureListener { e ->
                Log.d("HomeRepositoryImpl/dispatchRobot()", "Transaction failure.")
                throw InvalidDispatchException(
                    e.message ?: "[Transaction Failure] Cannot dispatch robots at the moment. "
                )
            }
            emit(Resource.Success(true))
        }.catch {
            Log.d("HomeRepositoryImpl/dispatchRobot()", it.message.toString())
            emit(Resource.Error(it.message.toString()))
        }
    }


//    override suspend fun fetchServiceArea(): Flow<Resource<ArrayList<ServiceArea>>> {
//        val serviceAreaArray: ArrayList<ServiceArea> = arrayListOf()
//        return flow {
//            emit(Resource.Loading())
//            db.collection("serviceArea")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        document.data[""]
//                        Log.d("HomeRepositoryImpl/fetchServiceArea()", "${document.id} => ${document.data}")
//                        serviceAreaArray.add(
//                            ServiceArea(
//                                areaCode = (document.data["areaCode"] as Long).toInt(),
//                                areaName = document.data["areaName"] as String,
//                                defaultCoordinate = document.data["defaultCoordinate"] as GeoPoint,
//                                defaultCameraZoomScale = (document.data["defaultCameraZoomScale"] as Long).toFloat()
//                            )
//                        )
//                    }
//                }
//                .addOnFailureListener { e ->
//                    throw InvalidServiceAreaException(
//                        e.message ?: "Cannot fetch serviceArea data from Firestore."
//                    )
//                }
//                .await()
//            emit(Resource.Success(serviceAreaArray))
//        }.catch {
//            emit(Resource.Error(it.message.toString()))
//        }
//    }
}