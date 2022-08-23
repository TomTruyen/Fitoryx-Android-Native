package com.tomtruyen.Fitoryx.service

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tomtruyen.Fitoryx.R

class GoogleAuthService(
    private val context: Context,
    private val client: SignInClient,
    private val clientId: String,
    private val oneTapIntentLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val fallbackIntentLauncher: ActivityResultLauncher<Intent>,
    private val onFailure: (String) -> Unit
    ) {
    fun signInWithGoogle() {
        client.beginSignIn(provideSignInRequest())
            .addOnSuccessListener {
                signInWithOneTap(it)
            }
            .addOnFailureListener {
                signUpWithOneTap()
            }
    }

    private fun signInWithOneTap(result: BeginSignInResult) {
        try {
            oneTapIntentLauncher.launch(
                IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
            )
        } catch (e: IntentSender.SendIntentException) {
            onFailure(context.getString(R.string.google_sign_in_failed, "OneTapIntent"))
        }
    }

    private fun signUpWithOneTap() {
        client.beginSignIn(provideSignUpRequest())
            .addOnSuccessListener {
                signInWithOneTap(it)
            }
            .addOnFailureListener {
                signInWithFallback()
            }
    }

    private fun signInWithFallback() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()

        val fallbackClient = GoogleSignIn.getClient(context, gso)

        fallbackIntentLauncher.launch(fallbackClient.signInIntent)
    }

    private fun provideSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientId)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun provideSignUpRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    companion object {
        fun signInWithCredentials(idToken: String?): Task<AuthResult> {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            return FirebaseAuth.getInstance().signInWithCredential(credential)
        }
    }
}