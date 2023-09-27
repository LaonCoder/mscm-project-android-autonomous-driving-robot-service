package com.example.mscmproject.feature_main.domain.model

import com.google.firebase.Timestamp

data class Dispatch(
    val robotCode: Int = 0,
    val user: String = "",
    val departure: String = "",
    val destination: String = "",
    val status: String = "",
    val timestamp: Timestamp = Timestamp(0L, 0)
)

class InvalidDispatchException(message: String): Exception(message)
class NoAvailableRobotException(message: String): Exception(message)