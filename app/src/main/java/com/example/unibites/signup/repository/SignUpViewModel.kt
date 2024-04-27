package com.example.unibites.signup.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore


class SignUpViewModel: ViewModel() {
    var uiState by mutableStateOf(SignUpState())

    var auth = Firebase.auth
    private val firestore = Firebase.firestore

    init {
        if(auth.currentUser == null){
            //
        } else {
            uiState = uiState.copy(registered = true)
        }
    }

    // prueba
    fun signUp(email: String, password: String, onSuccessRegister: () -> Unit,onErrorSignup: (errorMessage: String) -> Unit){
        uiState = uiState.copy(loading = true)
        val registrationAttemptData = hashMapOf(
            "email" to email,
            "timestamp" to FieldValue.serverTimestamp()
        )
        firestore.collection("registrationEvents").add(registrationAttemptData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Registration attempt logged with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error logging registration attempt", e)
            }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    uiState = uiState.copy(loading = false)
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        if (user != null) {
                            val userId = user.uid
                            createUserDocument(userId)
                            // Log the successful registration event
                            val registrationSuccessData = hashMapOf(
                                "userId" to userId,
                                "timestamp" to FieldValue.serverTimestamp()
                            )
                            firestore.collection("registrationEvents").add(registrationSuccessData)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "Successful registration logged with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error logging successful registration", e)
                                }
                        } else {
                            onErrorSignup("user is null")
                        }
                        onSuccessRegister()
                    } else {
                        Log.w(TAG, "createdUserWithEmail:failure", task.exception)
                        task.exception?.localizedMessage?.let { onErrorSignup(it.toString()) }
                        val registrationFailureData = hashMapOf(
                            "email" to email,
                            "error" to task.exception?.localizedMessage,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                        firestore.collection("registrationEvents").add(registrationFailureData)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "Failed registration logged with ID: ${documentReference.id}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error logging failed registration", e)
                            }


                    }
                }
    }
    private fun createUserDocument(userId: String) {
        val userDocument = firestore.collection("users").document(userId)
        val userData = hashMapOf(
            "preferences" to emptyList<String>()
        )
        userDocument.set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "User document created successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating user document", e)
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

