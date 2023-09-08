package com.example.mscmproject.feature_main.domain.model

import com.google.firebase.firestore.GeoPoint

data class ServicePoint (
    val pointName: String = "",
    val description: String = "",
    val coordinate: GeoPoint = GeoPoint(0.0, 0.0),
    val photoUrl: String = ""
)

class InvalidServicePointException(message: String): Exception(message)