package com.example.mscmproject.feature_main.domain.model

import com.google.firebase.firestore.GeoPoint

data class ServiceLocation(
    val areaCode: Int = 0,
    val location: String = "",
    val points: ArrayList<String> = arrayListOf(),
    val defaultCoordinate: GeoPoint = GeoPoint(0.0, 0.0),
    val defaultCameraZoomScale: Float = 0f
)

class InvalidServiceLocationException(message: String): Exception(message)