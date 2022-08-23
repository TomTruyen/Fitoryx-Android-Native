package com.tomtruyen.Fitoryx.service

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var oneTapClient: SignInClient? = null

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    fun signOut() {
        oneTapClient?.signOut()
        auth.signOut()
    }
}