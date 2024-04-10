package com.example.unibites.ui.snackdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SnackDetailViewModel : ViewModel() {
    var uiState by mutableStateOf(SnackDetailState())
    val db = Firebase.firestore
    init {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            withContext(Dispatchers.IO) {
                db.collection("restaurants").document("69tgPdNTgu70Y1yCSUve").get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val latitud = document.getDouble("lat")
                            val longitud = document.getDouble("long")
                            uiState = uiState.copy(
                                loading = false,
                                latitud = latitud ?: 0.0,
                                longitud = longitud ?: 0.0
                            )
                        }
                    }
            }
        }
    }
}

data class SnackDetailState (
    val loading: Boolean = false,
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)