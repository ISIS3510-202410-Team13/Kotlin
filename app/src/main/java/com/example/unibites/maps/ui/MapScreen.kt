package com.example.unibites.maps.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.unibites.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


val userLocation = LatLng(4.6029286, -74.0653713)
val restaurantLocation = LatLng(4.6029286, -74.0653713)

@Composable
fun MyUniMap(){
    val userLocationState = rememberMarkerState (position = userLocation)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(userLocation, 15f)
    val cameraPositionState = rememberCameraPositionState{
        position = defaultCameraPosition
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }
    val restaurantLocationState = rememberMarkerState(position = restaurantLocation)
    GoogleMap(modifier = Modifier.fillMaxSize(0.5f), properties = properties, uiSettings = MapUiSettings(zoomControlsEnabled = true), cameraPositionState = cameraPositionState){
        Marker(state = userLocationState,
            title = stringResource(id =R.string.tu_ubicacion),snippet = stringResource(R.string.en_este_momento_estas_aqui)
        ){}
        Marker(state = restaurantLocationState,
            title = stringResource(id = R.string.restaurante_destino),snippet = stringResource(R.string.sitio_destino_restaurante)
        ){}
    }
}