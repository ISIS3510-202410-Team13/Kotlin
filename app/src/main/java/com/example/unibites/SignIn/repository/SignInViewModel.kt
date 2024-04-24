package com.example.unibites.SignIn.repository

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SignInViewModel: ViewModel() {
    var uiState by mutableStateOf(SignInState())

    //var db = Firebase.firestore

    var auth = Firebase.auth


    init {
        if (auth.currentUser == null) {
            // No user is signed in
        } else {
            // User is signed in
            uiState = uiState.copy(loggedIn = true)
        }
    }


    fun signIn(email: String, password: String, onSuccessSignIn: () -> Unit, onErrorSignIn: () -> Unit){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful){
                val user = auth.currentUser
                onSuccessSignIn()
            }
            else {
                onErrorSignIn()
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}

data class SignInState(
    val loading: Boolean = false,
    val loggedIn: Boolean = false
)