package com.example.mscmproject.feature_main.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mscmproject.feature_auth.domain.util.Resource
import com.example.mscmproject.feature_main.presentation.home.components.HomeBottomSheet
import com.example.mscmproject.feature_main.presentation.home.components.HomeContent
import com.example.mscmproject.feature_main.presentation.home.components.HomeDrawerContent
import com.example.mscmproject.feature_main.presentation.home.components.PlaceSelectionDialog
import com.example.mscmproject.navigation.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val uiData by viewModel.homeUiData.collectAsStateWithLifecycle()

    val applicationContext = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(37.555369, 127.0463897), 15f
        )
    }

    LaunchedEffect(
        key1 = uiState.isSignOutSuccessful,
        key2 = uiState.isRevokeAccessSuccessful
    ) {
        if (uiState.isSignOutSuccessful || uiState.isRevokeAccessSuccessful) {
            Toast.makeText(
                applicationContext,
                if (uiState.isSignOutSuccessful) "Sign out successful" else "Revoke access successful",
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

    LaunchedEffect(
        key1 = uiState.signOutError,
        key2 = uiState.revokeAccessError
    ) {
        uiState.signOutError?.let { error ->
            Toast.makeText(
                applicationContext,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
        uiState.revokeAccessError?.let { error ->
            Toast.makeText(
                applicationContext,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Firestore [serviceLocation]에서 서비스 지역 정보 fetch
    LaunchedEffect(key1 = uiState.isInitialComposition) {
        if (uiState.isInitialComposition) {
            Log.d("HomeScreen/isInitialComposition", "True")
            viewModel.fetchServiceLocation()
            viewModel.checkInitialComposition()
        } else {
            Log.d("HomeScreen/isInitialComposition", "False")
        }
    }

    LaunchedEffect(
        key1 = uiState.isFetchServiceLocationSuccessful,
        key2 = uiData.serviceLocation
    ) {
        if (uiState.isFetchServiceLocationSuccessful) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(
                    uiData.serviceLocation!!.defaultCoordinate.latitude,
                    uiData.serviceLocation!!.defaultCoordinate.longitude
                ),
                uiData.serviceLocation!!.defaultCameraZoomScale
            )
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    ModalDrawerSheet {
                        HomeDrawerContent(
                            onNavBackButtonClick = { scope.launch { drawerState.close() } },
                            onUserInfoButtonClick = { /*TODO*/ },
                            onUsageInfoButtonClick = { /*TODO*/ },
                            onSignOutButtonClick = { viewModel.signOut() },
                            onRevokeAccessButtonClick = { viewModel.revokeAccess() },
                            onExitButtonClick = { /*TODO*/ }
                        )
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                HomeContent(
                    cameraPositionState = cameraPositionState,
                    uiData = uiData,
                    uiState = uiState,
                    onDrawerButtonClick = { scope.launch { drawerState.open() } },
                    onServiceLocationSelect = { location -> viewModel.selectServiceLocation(location) },
                    onServiceLocationSelectButtonClick = { viewModel.showServiceLocationDialog(true) },
                    onPlaceSelectionDialogDismiss = { viewModel.showServiceLocationDialog(false) },
                    onCallButtonClick = { viewModel.showHomeBottomSheet(true) },
                )

                if (uiState.showHomeBottomSheet && uiState.isFetchServiceLocationSuccessful) {
                    HomeBottomSheet(
                        bottomSheetState = bottomSheetState,
                        uiData = uiData,
                        onBottomSheetDismiss = { viewModel.showHomeBottomSheet(false) },
                        onHideButtonClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    viewModel.showHomeBottomSheet(false)
                                }
                            }
                        },
                        onDepartureSelectButtonClick = { viewModel.showDepartureDialog(true) },
                        onDestinationSelectButtonClick = { viewModel.showDestinationDialog(true) }
                    )
                }

                if (uiState.showDepartureDialog) {
                    PlaceSelectionDialog(
                        text = "출발지를 선택해 주세요.",
                        places = uiData.serviceLocation!!.points,
                        onDismiss = { viewModel.showDepartureDialog(false) },
                        onSelect = { point -> viewModel.selectDeparturePoint(point) }
                    )
                }

                if (uiState.showDestinationDialog) {
                    PlaceSelectionDialog(
                        text = "목적지를 선택해 주세요.",
                        places = uiData.serviceLocation!!.points,
                        onDismiss = { viewModel.showDestinationDialog(false) },
                        onSelect = { point -> viewModel.selectDestinationPoint(point) }
                    )
                }
            }
        }
    }
}