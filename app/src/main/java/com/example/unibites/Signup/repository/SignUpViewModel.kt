package com.example.unibites.Signup.repository

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel: ViewModel() {
    var uiState by mutableStateOf(SignUpState())

    var db = Firebase.firestore

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
                onSuccessSignIn()
            }
            else {
                onErrorSignIn()
            }
        }
    }
}

data class SignUpState(
    val loading: Boolean = false,
    val loggedIn: Boolean = false
)