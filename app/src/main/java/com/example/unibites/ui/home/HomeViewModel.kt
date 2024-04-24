package com.example.unibites.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibites.model.CollectionType
import com.example.unibites.model.Snack
import com.example.unibites.model.SnackCollection
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {
        var uiState by mutableStateOf(HomeState())
        val db = Firebase.firestore
        init {
                viewModelScope.launch {
                        uiState = uiState.copy(loading = true)
                        getSnacks {
                                uiState = uiState.copy(
                                loading = false,
                                objeto = listOf(SnackCollection(
                                        id = 1L,
                                        name = "Lo más buscado",
                                        type = CollectionType.Highlight,
                                        snacks = it
                                ), SnackCollection(
                                        id = 2L,
                                        name = "Restaurantes veganos",
                                        type = CollectionType.Highlight,
                                        snacks = it
                                ))
                        )}

                }
        }
        suspend fun getSnacks(onSuccess: (List<Snack>) -> Unit){
                withContext(Dispatchers.IO){
                        db.collection("restaurants").get()
                                .addOnSuccessListener { result ->
                                        val list = mutableListOf<Snack>()
                                        for (document in result) {
                                                Log.d(TAG, "${document.id} => ${document.data}")
                                                if (document != null) {
                                                        val latitud = document.getDouble("lat")
                                                        val longitud = document.getDouble("long")
                                                        val descripcion = document.getString("description")
                                                        val nombre = document.getString("name")
                                                        val imagen = document.getString("url")
                                                        val price = document.getString("price")
                                                        val rating = document.getString("rating")
                                                        list.add(Snack(
                                                                id = document.id,
                                                                name = nombre ?: "",
                                                                //descripcion = descripcion ?: "",
                                                                price = price ?: "",
                                                                rating = rating ?: "",
                                                                imageUrl = imagen ?: "",
                                                                lat = latitud ?: 0.0,
                                                                long = longitud ?: 0.0
                                                        ))
                                                }
                                        }
                                        onSuccess(list)
                                }
                }
        }
}

data class HomeState(
        val objeto: List<SnackCollection> = listOf(),
        val loading: Boolean = false
        )