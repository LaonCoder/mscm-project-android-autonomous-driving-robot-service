package com.example.mscmproject.feature_main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun PlaceSelectionDialog(
    text: String,
    places: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Surface(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .padding(vertical = 8.dp),
                    fontSize = 16.sp,
                    lineHeight = 17.sp
                )
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth()
                )
                if (places.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.LightGray)
                            .padding(10.dp)
                    ) {

                        val state = rememberLazyListState()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 12.dp)
                            ,
                            state = state
                        ) {
                            items(places) { place ->
                                Button(
                                    onClick = {
                                        onSelect(place)
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                ) {
                                    Text(text = place)
                                }
                            }
                        }
                        // Scroll bar
                    }
                } else {
                    Column {
                        Text(text = "선택 가능한 장소가 없습니다.")
                    }
                }
            }
        }
    }
}