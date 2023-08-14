package com.example.mscmproject.feature_main.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mscmproject.feature_main.presentation.home.components.HomeContent
import com.example.mscmproject.feature_main.presentation.home.components.HomeTopBar
import com.example.mscmproject.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val applicationContext = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.isSignOutSuccessful) {
        if(state.isSignOutSuccessful) {
            Toast.makeText(
                applicationContext,
                "Sign Out successful",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Main.route) {
                    inclusive = true
                }
            }
            viewModel.resetStates()
        }
    }

    LaunchedEffect(key1 = state.isSignOutSuccessful) {
        if(state.isSignOutSuccessful) {
            Toast.makeText(
                applicationContext,
                "Sign Out successful",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Main.route) {
                    inclusive = true
                }
            }
            viewModel.resetStates()
        }
    }

    LaunchedEffect(key1 = state.signOutError) {
        state.signOutError?.let { error ->
            Toast.makeText(
                applicationContext,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            HomeContent(
                padding = padding,
                photoUrl = viewModel.photoUrl,
                displayName = viewModel.displayName
            )
        },
    )
}