package com.example.mscmproject.feature_auth.presentation.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mscmproject.feature_auth.presentation.AuthViewModel
import com.example.mscmproject.navigation.Screen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController,
    activityScope: LifecycleCoroutineScope,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.signInState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val applicationContext = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit) {
        if(googleAuthUiClient.getSignedInUser() != null) {
            navController.navigate(Screen.Main.route)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == ComponentActivity.RESULT_OK) {
                activityScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if(state.isSignInSuccessful) {
            Toast.makeText(
                applicationContext,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Auth.route) {
                    inclusive = true
                }
            }
            viewModel.resetStates()
        }
    }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter your credential's to login",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color.Gray
        )
        TextField(
            value = state.email,
            onValueChange = { viewModel.onSignInEmailChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
            ,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                disabledLabelColor = Color.LightGray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text(text = "Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.password,
            onValueChange = { viewModel.onSignInPasswordChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
            ,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                disabledLabelColor = Color.LightGray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text(text = "Password") }
        )
        Button(
            onClick = {
                scope.launch {
                    viewModel.login(state.email.trim(), state.password.trim())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                .background(Color.DarkGray)
            ,
            colors= ButtonDefaults.buttonColors(
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = "Login", color = Color.White, modifier = Modifier.padding(7.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
        Text(
            text = "Already Have an Account? Sign Up",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                    viewModel.resetStates()
                }
        )
        Text(
            text = "or connect with",
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    activityScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            ) {
                Text(text = "Google Login")
            }
        }
    }
}