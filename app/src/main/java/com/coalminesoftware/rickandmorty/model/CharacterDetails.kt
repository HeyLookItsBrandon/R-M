package com.coalminesoftware.rickandmorty.model

import java.time.Instant

data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val origin: LocationReference?,
    val image: String,
    val created: Instant
)
