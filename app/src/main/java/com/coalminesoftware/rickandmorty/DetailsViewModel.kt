package com.coalminesoftware.rickandmorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coalminesoftware.rickandmorty.data.CharacterService
import com.coalminesoftware.rickandmorty.data.InstantTypeAdapter
import com.coalminesoftware.rickandmorty.data.Resource
import com.coalminesoftware.rickandmorty.model.CharacterDetails
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

class DetailsViewModel: ViewModel() {
    private val _characterResult = MutableStateFlow<Resource<CharacterDetails>>(Resource.Loading)
    val characterResult: StateFlow<Resource<CharacterDetails>> = _characterResult

    // Ideally, the service would be injected, not created by each ViewModel
    val gson = GsonBuilder()
        .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
        .create()
    val characterService = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(CharacterService::class.java)

    fun getCharacterDetails(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _characterResult.value = Resource.Loading
            try {
                _characterResult.value = Resource.Success(characterService.getCharacter(characterId))
            } catch (e: Exception) {
                _characterResult.value = Resource.Error("Failed to load character", e)
            }
        }
    }
}
