package com.example.mscmproject.feature_main.domain.model

import com.google.firebase.firestore.GeoPoint

data class ServiceArea(
    val areaCode: Int = 0,
    val areaName: String = "",
    val defaultCoordinate: GeoPoint = GeoPoint(0.0, 0.0),
    val defaultCameraZoomScale: Float = 0f,
    val servicePoints: ArrayList<ServicePoint> = arrayListOf(),
    val gpsPaths: ArrayList<GpsPath> = arrayListOf()
)

class InvalidServiceAreaException(message: String): Exception(message)