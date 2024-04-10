package com.example.unibites.maps.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel: ViewModel(){
   var uiState  by mutableStateOf(MapStateUi())
    init {
        uiState = uiState.copy(latitud = 4.6029286, longitud =  -74.0653713)
        viewModelScope.launch {
            var ubicacion = withContext(Dispatchers.IO){
                delay(2000)
                Pair(4.7104412, -74.1210308)
            }
            uiState = uiState.copy(latitud = ubicacion.first, longitud = ubicacion.second)
        }
    }
}
data class MapStateUi(
    val loading: Boolean = false,
    val error: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)