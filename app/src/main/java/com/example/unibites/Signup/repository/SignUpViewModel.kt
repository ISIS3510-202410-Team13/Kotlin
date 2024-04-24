package com.example.unibites.signup.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class SignUpViewModel: ViewModel() {
    var uiState by mutableStateOf(SignUpState())

    var auth = Firebase.auth

    init {
        if(auth.currentUser == null){
            //
        } else {
            uiState = uiState.copy(registered = true)
        }
    }

    fun signUp(email: String, password: String, onSuccessRegister: () -> Unit,onErrorSignup: (errorMessage: String) -> Unit){
        uiState = uiState.copy(loading = true)
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task ->
                    uiState = uiState.copy(loading = false)
                    if(task.isSuccessful){
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        onSuccessRegister()
                    }
                    else {
                        Log.w(TAG, "createdUserWithEmail:failure", task.exception)
                        task.exception?.localizedMessage?.let { onErrorSignup(it.toString()) }

                    }
                }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}


data class SignUpState(
        val loading: Boolean = false,
        val registered: Boolean = false
)

