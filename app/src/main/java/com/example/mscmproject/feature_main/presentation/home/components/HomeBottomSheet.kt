package com.example.mscmproject.feature_main.presentation.home.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.mscmproject.feature_main.presentation.home.HomeUiData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheet(
    bottomSheetState: SheetState,
    uiData: HomeUiData,
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
            onHideButtonClick = onHideButtonClick,
            onDepartureSelectButtonClick = onDepartureSelectButtonClick,
            onDestinationSelectButtonClick = onDestinationSelectButtonClick
        )
    }
}

@Composable
fun HomeBottomSheetContent(
    uiData: HomeUiData,
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
            Text(uiData.departureLocation ?: "출발지를 입력하세요.")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                onDestinationSelectButtonClick()
            }) {
            Text(uiData.destinationLocation ?: "목적지를 입력하세요.")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
        }) {
            Text("로봇 호출하기")
        }
    }
}