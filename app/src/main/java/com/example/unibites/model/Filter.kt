package com.example.unibites.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
class Filter(
    val name: String,
    enabled: Boolean = false,
    val icon: ImageVector? = null
) {
    val enabled = mutableStateOf(enabled)
}
val filters = listOf(
    Filter(name = "Organico"),
    Filter(name = "Libre de Gluten"),
    Filter(name = "Libre de Lactosa"),
    Filter(name = "Dulce"),
    Filter(name = "Salado")
)
val priceFilters = listOf(
    Filter(name = "$"),
    Filter(name = "$$"),
    Filter(name = "$$$"),
    Filter(name = "$$$$")
)
val sortFilters = listOf(
    Filter(name = "Lo más buscado", icon = Icons.Filled.Android),
    Filter(name = "Puntaje", icon = Icons.Filled.Star),
    Filter(name = "Alfabéticamente", icon = Icons.Filled.SortByAlpha)
)

val categoryFilters = listOf(
    Filter(name = "Cocina Italiana"),
    Filter(name = "Cocina Internacional"),
    Filter(name = "Cocina Colombiana"),
    Filter(name = "Cocina Rápida"),
)
val lifeStyleFilters = listOf(
    Filter(name = "Organico"),
    Filter(name = "Libre de Gluten"),
    Filter(name = "Libre de Lactosa"),
    Filter(name = "Dulce"),
    Filter(name = "Salado")
)

var sortDefault = sortFilters.get(0).name
