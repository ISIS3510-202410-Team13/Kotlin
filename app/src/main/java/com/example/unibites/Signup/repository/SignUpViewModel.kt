package com.example.unibites.Signup.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    var uiState by mutableStateOf(SignUpState())

    var db = Firebase.firestore

    init {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            db.collection("").get().addOnSuccessListener { result ->
                // TODO()
            }
        }
    }
}

data class SignUpState(
    val loading: Boolean = true
)