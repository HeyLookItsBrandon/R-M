package com.coalminesoftware.rickandmorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coalminesoftware.rickandmorty.data.CharacterService
import com.coalminesoftware.rickandmorty.data.InstantTypeAdapter
import com.coalminesoftware.rickandmorty.data.Resource
import com.coalminesoftware.rickandmorty.model.CharacterDetails
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel : ViewModel() {
    // Ideally, the service would be injected, not created by each ViewModel
    val gson = GsonBuilder()
        .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
        .create()
    val characterService = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(CharacterService::class.java)

    val searchQuery = MutableStateFlow("")

    private val _characterResults = MutableStateFlow<Resource<List<CharacterDetails>>>(Resource.Success(emptyList()))
    val characterResults: StateFlow<Resource<List<CharacterDetails>>> = _characterResults

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .onEach { _characterResults.value = Resource.Loading }
                .flatMapLatest { query ->
                    flow {
                        try {
                            val response = withContext(Dispatchers.IO) {
                                characterService.findCharacters(query)
                            }
                            emit(Resource.Success(response.results))
                        } catch (e: Exception) {
                            emit(Resource.Error("Failed to load characters", e))
                        }
                    }
                }.collect { result ->
                    _characterResults.value = result
                }
        }
    }
}
