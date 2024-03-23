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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


val userLocation = LatLng(4.6029286, -74.0653713)
val restaurantLocation = LatLng(4.6029286, -74.0653713)

@Composable
fun MapScreen() {
    val uniandes = LatLng(4.6029286, -74.0653713)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(uniandes, 15f)
    val cameraPositionState = rememberCameraPositionState{
        position = defaultCameraPosition
    }
    var isMapLoaded by remember { mutableStateOf( false) }
    Box(modifier = Modifier.fillMaxSize()){
        GoogleMapView(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true }
        )
        if(!isMapLoaded){
            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val userLocationState = rememberMarkerState (position = userLocation)
    val restaurantLocationState = rememberMarkerState(position = restaurantLocation)
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapLoaded
    ){
        Marker(
            state = userLocationState,
            title = "Tu localización"
        )
        MarkerInfoWindowContent(
            state = restaurantLocationState,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        ){
            Text(text = "Restaurante destino", color = Color.Gray)
        }

        content()
    }
}