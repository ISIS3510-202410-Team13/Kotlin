package com.example.unibites.SignIn.repositorio

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignInViewModel: ViewModel() {
    var uiState by mutableStateOf(SignInState())

    var auth = Firebase.auth


    init {
        if (auth.currentUser == null) {
            // No user is signed in
        } else {
            // User is signed in
            uiState = uiState.copy(loggedIn = true)
        }
    }


    fun signIn(email: String, password: String, onSuccessSignIn: () -> Unit, onErrorSignIn: (errorMessage: String) -> Unit){
        uiState = uiState.copy(loading = true)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            uiState = uiState.copy(loading = false)
            if (result.isSuccessful){
                val user = auth.currentUser
                onSuccessSignIn()
            }
            else {
                result.exception?.localizedMessage?.let { onErrorSignIn(it.toString()) }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun signInWithBiometric(onSuccessSignIn: () -> Unit, onErrorSignIn: (errorMessage: String) -> Unit) {

    }
}

data class SignInState(
    val loading: Boolean = false,
    val loggedIn: Boolean = false
)