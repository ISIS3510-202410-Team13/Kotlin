package com.example.unibites.model

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Immutable
data class SnackCollection(
    val id: Long,
    val name: String,
    val snacks: List<Snack>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }


object SnackRepo {

    fun getSnacks(): List<SnackCollection> = snackCollections
    fun getSnack(snackId: String) = snacks.find { it.id == snackId }!!
    fun getRelated(@Suppress("UNUSED_PARAMETER") snackId: String) = related
    fun getFilters() = filters
    fun getPriceFilters() = priceFilters
    fun getSortFilters() = sortFilters
    fun getCategoryFilters() = categoryFilters
    fun getSortDefault() = sortDefault
    fun getLifeStyleFilters() = lifeStyleFilters
}

/**
 * Static data
 */

private val tastyTreats = SnackCollection(
    id = 1L,
    name = "Lo más buscado",
    type = CollectionType.Highlight,
    snacks = snacks.subList(0, 4)
)

private val popular = SnackCollection(
    id = 2L,
    name = "Popular en UniBites",
    snacks = snacks.subList(0,1)
)

private val opcionesVeganas = tastyTreats.copy(
    id = 3L,
    name = "Opciones Veganas"
)



private val snackCollections = listOf(
    tastyTreats,
    popular,
    opcionesVeganas
)

private val related = listOf(
    popular
)


