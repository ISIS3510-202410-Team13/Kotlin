package com.example.unibites.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.unibites.model.CollectionType
import com.example.unibites.model.Snack
import com.example.unibites.model.SnackCollection
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {
        var uiState by mutableStateOf(HomeState())
        var navController by mutableStateOf<NavController?>(null)
        private val db = Firebase.firestore.apply {
                firestoreSettings = FirebaseFirestoreSettings.Builder()
                        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                        .build()
        }

        init {
                viewModelScope.launch {
                        uiState = uiState.copy(loading = true)
                        val snacks = getSnacks()
                        uiState = uiState.copy(
                                loading = false,
                                objeto = listOf(
                                        SnackCollection(
                                                id = 1L,
                                                name = "Lo más buscado",
                                                type = CollectionType.Highlight,
                                                snacks = snacks
                                        ),
                                        SnackCollection(
                                                id = 2L,
                                                name = "Restaurantes veganos",
                                                type = CollectionType.Highlight,
                                                snacks = snacks
                                        )
                                )
                        )
                }
        }

        private suspend fun getSnacks(): List<Snack> {
                return try {
                        val result = db.collection("restaurants").get().await()
                        result.documents.mapNotNull { document ->
                                val latitud = document.getDouble("lat")
                                val longitud = document.getDouble("long")
                                val description = document.getString("description")
                                val nombre = document.getString("name")
                                val imagen = document.getString("url")
                                val price = document.getString("price")
                                val rating = document.getString("rating")
                                if (latitud != null && longitud != null) {
                                        Snack(
                                                id = document.id,
                                                name = nombre.orEmpty(),
                                                description = description.orEmpty(),
                                                price = price.orEmpty(),
                                                rating = rating.orEmpty(),
                                                imageUrl = imagen.orEmpty(),
                                                lat = latitud,
                                                long = longitud
                                        )
                                } else null
                        }
                } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error fetching snacks", e)
                        emptyList()
                }
        }

        fun retrieveSnacks(): List<SnackCollection> = uiState.objeto

        fun getSnack(snackId: String): Snack? {
                return uiState.objeto.flatMap { it.snacks }.find { it.id == snackId }
        }
}

data class HomeState(
        val objeto: List<SnackCollection> = emptyList(),
        val loading: Boolean = false
)
