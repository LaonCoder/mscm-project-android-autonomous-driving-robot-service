package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mscmproject.feature_main.presentation.home.HomeUiData
import com.example.mscmproject.feature_main.presentation.home.HomeUiState
import com.google.maps.android.compose.CameraPositionState

@Composable
fun HomeContent(
    cameraPositionState: CameraPositionState,
    uiData: HomeUiData,
    uiState: HomeUiState,
    onDrawerButtonClick: () -> Unit,
    onServiceLocationSelectButtonClick: () -> Unit,
    onPlaceSelectionDialogDismiss: () -> Unit,
    onServiceAreaSelect: (String) -> Unit,
    onCallButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMapContent(
            uiData = uiData,
            uiState = uiState,
            cameraPositionState = cameraPositionState
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(15.dp)
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                ,
                onClick = { onServiceLocationSelectButtonClick() }
            ) {
                Text( uiData.serviceArea?.areaName ?: "서비스 지역을 선택해 주세요." )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                onClick = { onCallButtonClick() }) {
                Text("로봇 호출하기")
            }
        }
        Button(
            onClick = { onDrawerButtonClick() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 30.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }

        if (uiState.showServiceAreaDialog) {
            PlaceSelectionDialog(
                text = "서비스 지역을 선택해주세요.",
                places = uiData.serviceAreas.map { it.areaName },
                onDismiss = {
                    onPlaceSelectionDialogDismiss()
                },
                onSelect = { area ->
                    onServiceAreaSelect(area)
                }
            )
        }
    }
}