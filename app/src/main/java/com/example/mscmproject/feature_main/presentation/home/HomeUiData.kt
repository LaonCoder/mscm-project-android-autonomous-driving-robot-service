package com.example.mscmproject.feature_main.presentation.home

import com.example.mscmproject.feature_main.domain.model.ServiceLocation

data class HomeUiData(
    // available service locations
    val serviceLocations: ArrayList<ServiceLocation> = arrayListOf(),
    // place selection
    val serviceLocation: ServiceLocation? = null,
    val departureLocation: String? = null,
    val destinationLocation: String? = null,
)
