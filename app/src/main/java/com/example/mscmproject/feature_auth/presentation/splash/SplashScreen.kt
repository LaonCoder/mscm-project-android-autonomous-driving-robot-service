package com.example.mscmproject.feature_auth.presentation.splash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mscmproject.MainActivity
import com.example.mscmproject.feature_auth.presentation.AuthViewModel
import com.example.mscmproject.navigation.Screen
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlin.system.exitProcess

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    /** Transition **/
    val scale = remember { Animatable(0f) }
    val overshootInterpolator = remember { OvershootInterpolator(2f) }

    /** Permission **/
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            Log.d("Location Permission", "Permission Granted")
        }
        else {
            Log.d("Location Permission", "Permission Denied")
            MainActivity().finish()
            exitProcess(1)
        }
    }

    LaunchedEffect(key1 = true) {
        checkAndRequestPermissions(
            context,
            permissions,
            launcherMultiplePermissions
        )
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    overshootInterpolator.getInterpolation(it)
                }
            )
        )
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Login.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF253EDB), Color(0xFF1939B7))
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Text("Logo")
//                Image(
//                    painter = painterResource(id = ),
//                    contentDescription = "Logo",
//                    modifier = Modifier.weight(3f)
//                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        Log.d("Location Permission", "Permission Exists")
    }
    else {
        launcher.launch(permissions)
        Log.d("Location Permission", "Permission Requested")
    }
}

// 프로그램 강제 종료
// MainActivity().finish()
// exitProcess(1)