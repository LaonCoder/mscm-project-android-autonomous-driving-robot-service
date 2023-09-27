package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HomeDrawerContent(
    onNavBackButtonClick: () -> Unit,
    onUserInfoButtonClick: () -> Unit,
    onUsageInfoButtonClick: () -> Unit,
    onSignOutButtonClick: () -> Unit,
    onRevokeAccessButtonClick: () -> Unit,
    onExitButtonClick: () -> Unit
) {
    Box(
    modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 64.dp),
    ) {
        IconButton(
            onClick = { onNavBackButtonClick() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate back",
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp, 32.dp)
            )
        }
        Text("Logo", modifier = Modifier.align(Alignment.Center))
    }
    Button(
        onClick = { onUserInfoButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text("사용자 정보 수정")
    }
    Button(
        onClick = { onUsageInfoButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text("사용 방법 안내")
    }
    Button(
        onClick = { onSignOutButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text("로그아웃")
    }
    Button(
        onClick = { onRevokeAccessButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text("계정 삭제")
    }
    Button(
        onClick = { onExitButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text("EXIT")
    }

    // TODO : Use only for testing!!!
    // DataUploadButton()
}