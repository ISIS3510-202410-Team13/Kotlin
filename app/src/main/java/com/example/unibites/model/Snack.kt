package com.example.unibites.model

import androidx.compose.runtime.Immutable


@Immutable
data class Snack(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val tagline: String = "",
    val tags: Set<String> = emptySet()
)


val snacks = listOf(
    Snack(
        id = 1L,
        name = "Toninos",
        tagline = "Cocina Italiana",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
    ),
    Snack(
        id = 2L,
        name = "Crepes & Waffles",
        tagline = "Cocina Int.",
        imageUrl = "https://source.unsplash.com/Yc5sL-ejk6U",
    ),
    Snack(
        id = 3L,
        name = "Don Jediondo",
        tagline = "Cocina Col.",
        imageUrl = "https://source.unsplash.com/-LojFX9NfPY",
    ),
    Snack(
        id = 4L,
        name = "El Corral",
        tagline = "Cocina Rápida",
        imageUrl = "https://source.unsplash.com/3U2V5WqK1PQ",
    ),
    Snack(
        id = 5L,
        name = "Subway",
        tagline = "Cocina Rápida",
        imageUrl = "https://source.unsplash.com/Y4YR9OjdIMk",
    ),
    Snack(
        id = 6L,
        name = "Cinnabon",
        tagline = "Cocina Rápida",
        imageUrl = "https://source.unsplash.com/bELvIg_KZGU",
    ),
    Snack(
        id = 7L,
        name = "Dunkin Donuts",
        tagline = "Cocina Rápida",
        imageUrl = "https://source.unsplash.com/YgYJsFDd4AU",
    ),
    Snack(
        id = 8L,
        name = "Frisby",
        tagline = "Cocina Rápida",
        imageUrl = "https://source.unsplash.com/0u_vbeOkMpk",
    ),
    Snack(
        id = 9L,
        name = "One Burrito",
        tagline = "Cocina Méxicana",
        imageUrl = "https://source.unsplash.com/yb16pT5F_jE",
    ),
    Snack(
        id = 10L,
        name = "La Puerta",
        tagline = "Cocina Mexicana",
        imageUrl = "https://source.unsplash.com/AHF_ZktTL6Q",
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
//    )
)
