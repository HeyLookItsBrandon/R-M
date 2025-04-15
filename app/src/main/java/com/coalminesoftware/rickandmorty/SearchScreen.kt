package com.coalminesoftware.rickandmorty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.coalminesoftware.rickandmorty.data.Resource
import com.coalminesoftware.rickandmorty.model.CharacterDetails
import com.coalminesoftware.rickandmorty.ui.theme.RickAndMortyTheme
import java.time.Instant

@Composable
fun SearchScreen(detailsNavCallback: (Int)->Unit) {
    val viewModel: SearchViewModel = viewModel()
    val searchQuery = viewModel.searchQuery.collectAsState()
    val searchResult by viewModel.characterResults.collectAsState()
    val isLoading = searchResult is Resource.Loading
    val resultList: List<CharacterDetails> = when ( val state = searchResult) {
        is Resource.Success -> state.data
        else -> emptyList()
    }

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery.value,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text(stringResource(R.string.search)) },
                modifier = Modifier.fillMaxWidth())
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.CenterEnd)
                .alpha(if (isLoading) 1f else 0f))
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(resultList) { character ->
                CharacterCard(character, detailsNavCallback)
            }
        }
    }
}

@Composable
fun CharacterCard(character: CharacterDetails, detailsNavCallback: (Int)->Unit) {
    Card (modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clickable(onClick = { detailsNavCallback(character.id) })
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = character.image,
                contentDescription = stringResource(R.string.picture_of, character.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize())
            Text(
                text = character.name,
                modifier = Modifier.align(Alignment.BottomStart))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    RickAndMortyTheme {
        SearchScreen {}
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPreview() {
    RickAndMortyTheme {
        CharacterCard(CharacterDetails(0, "Rick", "", "", "", null, "https://rickandmortyapi.com/api/character/avatar/1.jpeg", Instant.now())) {}
    }
}
