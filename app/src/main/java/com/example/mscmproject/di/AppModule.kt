package com.example.mscmproject.di

import android.app.Application
import android.content.Context
import com.example.mscmproject.BuildConfig.WEB_CLIENT_ID
import com.example.mscmproject.core.Constants.SIGN_IN_REQUEST
import com.example.mscmproject.core.Constants.SIGN_UP_REQUEST
import com.example.mscmproject.feature_auth.data.repository.AuthRepositoryImpl
import com.example.mscmproject.feature_auth.domain.repository.AuthRepository
import com.example.mscmproject.feature_main.data.repository.HomeRepositoryImpl
import com.example.mscmproject.feature_main.domain.repository.HomeRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = Firebase.firestore

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun providesSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(false)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun providesSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()


    @Provides
    fun providesGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(WEB_CLIENT_ID)
        .requestEmail()
        .build()

    @Provides
    fun providesGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(
        firebaseAuth = firebaseAuth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        db = db
    )

    @Provides
    fun providesHomeRepository(
        firebaseAuth: FirebaseAuth,
        oneTapClient: SignInClient,
        signInClient: GoogleSignInClient,
        db: FirebaseFirestore
    ): HomeRepository = HomeRepositoryImpl(
        firebaseAuth = firebaseAuth,
        oneTapClient = oneTapClient,
        signInClient = signInClient,
        db = db
    )

    @Provides
    @Singleton
    fun providesOneTapClient(
        @ApplicationContext
        context: Context,
    ) = Identity.getSignInClient(context)
}