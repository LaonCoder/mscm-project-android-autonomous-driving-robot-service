package com.example.mscmproject.feature_main.presentation.home

import com.example.mscmproject.feature_main.domain.model.GpsPath
import com.example.mscmproject.feature_main.domain.model.ServiceArea
import com.google.firebase.auth.FirebaseUser

data class HomeUiData(
    val currentUser: FirebaseUser? = null,

    // service area
    val serviceAreas: ArrayList<ServiceArea> = arrayListOf(),
    val serviceArea: ServiceArea? = null,

    // gps path
    val gpsPaths: ArrayList<GpsPath> = arrayListOf(),
    val gpsPath: GpsPath? = null,

    // selected points
    val departurePoint: String? = null,
    val destinationPoint: String? = null,
)
