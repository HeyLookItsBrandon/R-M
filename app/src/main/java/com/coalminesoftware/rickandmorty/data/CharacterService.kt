package com.coalminesoftware.rickandmorty.data

import com.coalminesoftware.rickandmorty.model.CharacterDetails
import com.coalminesoftware.rickandmorty.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Retrofit service for retrieving characters */
interface CharacterService {
    @GET("api/character/")
    suspend fun findCharacters(@Query("name") nameQuery: String): CharacterResponse

    @GET("api/character/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): CharacterDetails
}
