package com.example.unibites.preferences.repository

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class PreferencesViewModel: ViewModel() {
    var uiState by mutableStateOf(PreferencesState())

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun savePreferences(
        priceRange: Int,
        distanceLimit: Int,
        isVegan: Boolean,
        allergies: List<String>,
        onSuccessSave: () -> Unit,
        onErrorSave: (errorMessage: String) -> Unit
    ) {
        uiState = uiState.copy(loading = true)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userPreferences = hashMapOf(
                "priceRange" to priceRange,
                "distanceLimit" to distanceLimit,
                "isVegan" to isVegan,
                "allergies" to allergies
            )

            firestore.collection("users").document(userId)
                .update("preferences", userPreferences)
                .addOnSuccessListener {
                    uiState = uiState.copy(loading = false, preferencesSaved = true)
                    onSuccessSave()
                    val preferencesSaveSuccessData = hashMapOf(
                        "userId" to userId,
                        "preferences" to userPreferences,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                    firestore.collection("preferencesEvents").add(preferencesSaveSuccessData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Successful preferences save logged with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error logging successful preferences save", e)
                        }
                }
                .addOnFailureListener { e ->
                    uiState = uiState.copy(loading = false)
                    onErrorSave(e.localizedMessage ?: "Unknown error")
                    val preferencesSaveFailureData = hashMapOf(
                        "userId" to userId,
                        "error" to e.localizedMessage,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                    firestore.collection("preferencesEvents").add(preferencesSaveFailureData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Failed preferences save logged with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error logging failed preferences save", e)
                        }

                }
        } else {
            uiState = uiState.copy(loading = false)
            onErrorSave("User not authenticated")
            val preferencesSaveNoAuthData = hashMapOf(
                "error" to "User not authenticated",
                "timestamp" to FieldValue.serverTimestamp()
            )
            firestore.collection("preferencesEvents").add(preferencesSaveNoAuthData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Preferences save attempt without authentication logged with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error logging preferences save attempt without authentication", e)
                }
        }
    }

    companion object {
        private const val TAG = "PreferencesViewModel"
    }

}

data class PreferencesState(
    val loading: Boolean = false,
    val preferencesSaved: Boolean = false,
    val userPreferences: HashMap<String, Any> = HashMap()
)