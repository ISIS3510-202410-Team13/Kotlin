package com.example.unibites.model

import androidx.compose.runtime.Immutable


@Immutable
data class Snack(
    val id: String,
    val name: String,
    val description: String = "",
    val imageUrl: String,
    val tagline: String = "",
    val tags: Set<String> = emptySet(),
    val price: String,
    val rating: String,
    val lat: Double,
    val long: Double,
    val type: String = ""
)


val snacks = listOf<Snack>(
    Snack(
        id = "1",
        name = "Toninos",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        tagline = "Cocina Italiana",
        price =  "$",
        rating =  "5",
        lat =  0.0,
        long = 0.0,
        description = "Restaurante de comida italiana"
    ),
    Snack(
        id = "2",
        name = "Toninos",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        tagline = "Cocina Italiana",
        price =  "$",
        rating =  "5",
        lat =  0.0,
        long = 0.0,
    )
)
    /*Snack(
        id = 1L,
        name = "Toninos",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        tagline = "Cocina Italiana",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 2L,
        name = "Crepes & Waffles",
        imageUrl = "https://source.unsplash.com/Yc5sL-ejk6U",
        tagline = "Cocina Int.",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 3L,
        name = "Don Jediondo",
        imageUrl = "https://source.unsplash.com/-LojFX9NfPY",
        tagline = "Cocina Col.",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 4L,
        name = "El Corral",
        imageUrl = "https://source.unsplash.com/3U2V5WqK1PQ",
        tagline = "Cocina Rápida",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 5L,
        name = "Subway",
        imageUrl = "https://source.unsplash.com/Y4YR9OjdIMk",
        tagline = "Cocina Rápida",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 6L,
        name = "Cinnabon",
        imageUrl = "https://source.unsplash.com/bELvIg_KZGU",
        tagline = "Cocina Rápida",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 7L,
        name = "Dunkin Donuts",
        imageUrl = "https://source.unsplash.com/YgYJsFDd4AU",
        tagline = "Cocina Rápida",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 8L,
        name = "Frisby",
        imageUrl = "https://source.unsplash.com/0u_vbeOkMpk",
        tagline = "Cocina Rápida",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 9L,
        name = "One Burrito",
        imageUrl = "https://source.unsplash.com/yb16pT5F_jE",
        tagline = "Cocina Méxicana",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
    Snack(
        id = 10L,
        name = "La Puerta",
        imageUrl = "https://source.unsplash.com/AHF_ZktTL6Q",
        tagline = "Cocina Mexicana",
        price = price ?: "",
        rating = rating ?: 0.0,
        lat = latitud ?: 0.0,
        long = longitud ?: 0.0,
    ),
//    Snack(
//        id = 11L,
//        name = "Marshmallow",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/rqFm0IgMVYY",
//    ),
//    Snack(
//        id = 12L,
//        name = "Nougat",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/qRE_OpbVPR8",
//    ),
//    Snack(
//        id = 13L,
//        name = "Oreo",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/33fWPnyN6tU",
//    ),
//    Snack(
//        id = 14L,
//        name = "Pie",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/aX_ljOOyWJY",
//    ),
//    Snack(
//        id = 15L,
//        name = "Chips",
//        imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E",
//    ),
//    Snack(
//        id = 16L,
//        name = "Pretzels",
//        imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms",
//    ),
//    Snack(
//        id = 17L,
//        name = "Smoothies",
//        imageUrl = "https://source.unsplash.com/m741tj4Cz7M",
//    ),
//    Snack(
//        id = 18L,
//        name = "Popcorn",
//        imageUrl = "https://source.unsplash.com/iuwMdNq0-s4",
//    ),
//    Snack(
//        id = 19L,
//        name = "Almonds",
//        imageUrl = "https://source.unsplash.com/qgWWQU1SzqM",
//    ),
//    Snack(
//        id = 20L,
//        name = "Cheese",
//        imageUrl = "https://source.unsplash.com/9MzCd76xLGk",
//    ),
//    Snack(
//        id = 21L,
//        name = "Apples",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/1d9xXWMtQzQ",
//    ),
//    Snack(
//        id = 22L,
//        name = "Apple sauce",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/wZxpOw84QTU",
//    ),
//    Snack(
//        id = 23L,
//        name = "Apple chips",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/okzeRxm_GPo",
//    ),
//    Snack(
//        id = 24L,
//        name = "Apple juice",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/l7imGdupuhU",
//    ),
//    Snack(
//        id = 25L,
//        name = "Apple pie",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/bkXzABDt08Q",
//    ),
//    Snack(
//        id = 26L,
//        name = "Grapes",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/y2MeW00BdBo",
//    ),
//    Snack(
//        id = 27L,
//        name = "Kiwi",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/1oMGgHn-M8k",
//    ),
//    Snack(
//        id = 28L,
//        name = "Mango",
//        tagline = "A tag line",
//        imageUrl = "https://source.unsplash.com/TIGDsyy0TK4",
//    )**/

