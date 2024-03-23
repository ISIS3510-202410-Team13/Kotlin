/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.unibites.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * A fake repo for searching.
 */
object SearchRepo {
    fun getCategories(): List<SearchCategoryCollection> = searchCategoryCollections
    fun getSuggestions(): List<SearchSuggestionGroup> = searchSuggestions

    suspend fun search(query: String): List<Snack> = withContext(Dispatchers.Default) {
        delay(200L) // simulate an I/O delay
        snacks.filter { it.name.contains(query, ignoreCase = true) }
    }
}

@Immutable
data class SearchCategoryCollection(
    val id: Long,
    val name: String,
    val categories: List<SearchCategory>
)

@Immutable
data class SearchCategory(
    val name: String,
    val imageUrl: String
)

@Immutable
data class SearchSuggestionGroup(
    val id: Long,
    val name: String,
    val suggestions: List<String>
)

/**
 * Static data
 */

private val searchCategoryCollections = listOf(
    SearchCategoryCollection(
        id = 0L,
        name = "Categorías",
        categories = listOf(
            SearchCategory(
                name = "Cocina Italiana",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            ),
            SearchCategory(
                name = "Cocina Internacional",
                imageUrl = "https://source.unsplash.com/SfP1PtM9Qa8"
            ),
            SearchCategory(
                name = "Cocina Colombiana",
                imageUrl = "https://source.unsplash.com/_jk8KIyN_uA"
            ),
            SearchCategory(
                name = "Cocina Rápida",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            )
        )
    ),
    SearchCategoryCollection(
        id = 1L,
        name = "Estilos de Vida",
        categories = listOf(
            SearchCategory(
                name = "Organico",
                imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms"
            ),
            SearchCategory(
                name = "Libre de gluten",
                imageUrl = "https://source.unsplash.com/m741tj4Cz7M"
            ),
            SearchCategory(
                name = "Libre de lácteos",
                imageUrl = "https://source.unsplash.com/dt5-8tThZKg"
            ),
            SearchCategory(
                name = "Vegano",
                imageUrl = "https://source.unsplash.com/ReXxkS1m1H0"
            ),
            SearchCategory(
                name = "Vegitariano",
                imageUrl = "https://source.unsplash.com/IGfIGP5ONV0"
            ),
            SearchCategory(
                name = "Pesca Sostenible",
                imageUrl = "https://source.unsplash.com/9MzCd76xLGk"
            )
        )
    )
)

private val searchSuggestions = listOf(
    SearchSuggestionGroup(
        id = 0L,
        name = "Búsquedas recientes",
        suggestions = listOf(
            "Comida Italiana",
            "Comida Japonesa",
        )
    ),
    SearchSuggestionGroup(
        id = 1L,
        name = "Búsquedas populares",
        suggestions = listOf(
            "Organico",
            "Libre de gluten",
            "Paleo",
            "Vegano",
            "Vegitariano",
            "Whole30"
        )
    )
)
