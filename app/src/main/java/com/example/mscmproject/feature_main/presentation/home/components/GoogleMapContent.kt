package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mscmproject.feature_main.presentation.home.HomeUiData
import com.example.mscmproject.feature_main.presentation.home.HomeUiState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline


@Composable
fun GoogleMapContent(
    uiData: HomeUiData,
    uiState: HomeUiState,
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
        ,
        cameraPositionState = cameraPositionState
    ) {
        if (uiState.isFetchServiceAreaSuccessful) {
            for (point in uiData.serviceArea!!.servicePoints) {
                Marker(
                    state = (
                        MarkerState(
                            position = LatLng(
                                point.coordinate.latitude,
                                point.coordinate.longitude
                            )
                        )
                    ),
                    title = point.pointName,
                    snippet = point.description,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        when(point.pointName) {
                            uiData.destinationPoint -> BitmapDescriptorFactory.HUE_GREEN
                            uiData.departurePoint -> BitmapDescriptorFactory.HUE_RED
                            else -> BitmapDescriptorFactory.HUE_ORANGE
                        }
                    )
                )
            }

            if (
                uiState.hasAvailableGpsPath &&
                uiData.gpsPath != null
            ) {
                Polyline(
                    points = uiData.gpsPath.track.map {
                        LatLng( it.latitude, it.longitude )
                    },
                    color = Color.Red,
                    pattern = listOf(Dash(20f), Gap(20f))
                )
            }
        }
    }
}