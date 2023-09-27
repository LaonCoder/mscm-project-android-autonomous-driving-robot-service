package com.example.mscmproject.feature_main.domain.model

import com.google.firebase.firestore.GeoPoint

data class GpsPath(
    val pathName: String = "",
    val track: ArrayList<GeoPoint> = arrayListOf()
)

class InvalidGpsPathException(message: String): Exception(message)