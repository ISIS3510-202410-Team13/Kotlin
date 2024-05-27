package com.example.unibites.ui.home

import android.content.ContentValues.TAG
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {
        var uiState by mutableStateOf(HomeState())
        var navController by mutableStateOf<NavController?>(null)
        var db = Firebase.firestore

        // The default cache size threshold is 100 MB. Configure "setCacheSizeBytes"
        // for a different threshold (minimum 1 MB) or set to "CACHE_SIZE_UNLIMITED"
        // to disable clean-up.


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
                val settings = FirebaseFirestoreSettings.Builder()
                        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                        .build()
                db.firestoreSettings = settings
                withContext(Dispatchers.IO){
                        db.collection("restaurants").get()
                                .addOnSuccessListener { result ->
                                        val list = mutableListOf<Snack>()
                                        for (document in result) {
                                                Log.d(TAG, "${document.id} => ${document.data}")
                                                if (document != null) {
                                                        val latitud = document.getDouble("lat")
                                                        val longitud = document.getDouble("long")
                                                        val description = document.getString("description")
                                                        val nombre = document.getString("name")
                                                        val imagen = document.getString("url")
                                                        val price = document.getString("price")
                                                        val rating = document.getString("rating")
                                                        list.add(Snack(
                                                                id = document.id,
                                                                name = nombre  ?: "",
                                                                description = description ?: "",
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

        fun retrieveSnacks(): List<SnackCollection>{
                return uiState.objeto
        }

        fun getSnack(snackId: String): Snack?{
                var searchedSnack: Snack? = null
                var exit = false
                for (snackCollection in uiState.objeto){
                        for (snack in snackCollection.snacks){
                                if (snack.id == snackId){
                                        searchedSnack= snack
                                        exit = true
                                        break
                                }
                        }
                        if (exit){
                                break
                        }
                }
                return searchedSnack
        }

        fun retrieveSnacks(): List<SnackCollection>{
                return uiState.objeto
        }

        fun getSnack(snackId: String): Snack?{
                var searchedSnack: Snack? = null
                var exit = false
                for (snackCollection in uiState.objeto){
                        for (snack in snackCollection.snacks){
                                if (snack.id == snackId){
                                        searchedSnack= snack
                                        exit = true
                                        break
                                }
                        }
                        if (exit){
                                break
                        }
                }
                return searchedSnack
        }
}

data class HomeState(
        val objeto: List<SnackCollection> = listOf(),
        val loading: Boolean = false
        )