package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mscmproject.feature_main.presentation.home.HomeUiData
import com.example.mscmproject.feature_main.presentation.home.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheet(
    bottomSheetState: SheetState,
    uiData: HomeUiData,
    uiState: HomeUiState,
    onBottomSheetDismiss: () -> Unit,
    onHideButtonClick: () -> Unit,
    onDepartureSelectButtonClick: () -> Unit,
    onDestinationSelectButtonClick: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        sheetState = bottomSheetState,
        onDismissRequest = { onBottomSheetDismiss() },
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BottomSheetDefaults.DragHandle()
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
            }
        }
    ) {
        HomeBottomSheetContent(
            uiData = uiData,
            uiState = uiState,
            onHideButtonClick = onHideButtonClick,
            onDepartureSelectButtonClick = onDepartureSelectButtonClick,
            onDestinationSelectButtonClick = onDestinationSelectButtonClick
        )
    }
}

@Composable
fun HomeBottomSheetContent(
    uiData: HomeUiData,
    uiState: HomeUiState,
    onHideButtonClick: () -> Unit,
    onDepartureSelectButtonClick: () -> Unit,
    onDestinationSelectButtonClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ,
        onClick = onHideButtonClick
    ) {
        Text(text = "Cancel")
    }
    Column {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                onDepartureSelectButtonClick()
            }) {
            Text(uiData.departurePoint ?: "출발지를 입력하세요.")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                onDestinationSelectButtonClick()
            }) {
            Text(uiData.destinationPoint ?: "목적지를 입력하세요.")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                if (uiState.hasAvailableGpsPath) Color.Green else Color.LightGray
            ),
            onClick = {
                if (uiState.hasAvailableGpsPath) {
                    // TODO: Robot call transaction
                }
            }) {
            Text(
                text = if (uiState.hasAvailableGpsPath) "로봇 호출하기" else "호출 가능한 경로가 없습니다."
            )
        }
    }
}