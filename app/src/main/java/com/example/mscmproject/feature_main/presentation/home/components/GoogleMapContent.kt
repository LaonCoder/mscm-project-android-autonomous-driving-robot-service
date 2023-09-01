package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun GoogleMapContent(
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
        ,
        cameraPositionState = cameraPositionState
    ) {
//        Marker(
//            state = MarkerState(position = hanyangUniv),
//            title = "Hanyang Univ",
//            snippet = "Marker in Hanyang Univ"
//        )
    }

}