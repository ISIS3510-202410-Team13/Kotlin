package com.example.unibites.maps.ui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.unibites.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val userLocation = LatLng(4.6029286, -74.0653713)

@Composable
fun MyUniMap(latitud: Double, longitud: Double){
    var ubicacionRerstaurante = LatLng(latitud, longitud)
    val userLocationState = rememberMarkerState (position = userLocation)
    val defaultCameraPosition = remember {
        CameraPosition.fromLatLngZoom(ubicacionRerstaurante, 17f)
    }
    val cameraPositionState = rememberCameraPositionState{
        position = defaultCameraPosition
    }
    val properties = remember {
        MapProperties(mapType = MapType.NORMAL)
    }
    val restaurantLocationState = rememberMarkerState(position = ubicacionRerstaurante)

    GoogleMap(modifier = Modifier.fillMaxWidth(), properties = properties, uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true), cameraPositionState = cameraPositionState){
        Marker(state = userLocationState,
            title = stringResource(id =R.string.tu_ubicacion),snippet = stringResource(R.string.en_este_momento_estas_aqui)
            ){}
            Marker(state = restaurantLocationState,
                title = stringResource(id = R.string.restaurante_destino),snippet = stringResource(R.string.sitio_destino_restaurante)
            ){}
        }

    LaunchedEffect(key1 = restaurantLocationState.position) {
        Log.d(TAG, "New coordinates: ${restaurantLocationState.position}")
    }
}

