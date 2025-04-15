package com.coalminesoftware.rickandmorty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.coalminesoftware.rickandmorty.data.Resource
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun DetailsScreen(characterId: Int, viewModel: DetailsViewModel = viewModel()) {
    val characterDetails by viewModel.characterResult.collectAsState(Resource.Loading)

    LaunchedEffect(characterId) {
        viewModel.getCharacterDetails(characterId)
    }

    when (val result = characterDetails) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            val character = result.data

            Column {
                Text(character.name, style = MaterialTheme.typography.headlineLarge)
                AsyncImage(
                    model = character.image,
                    contentDescription = stringResource(R.string.picture_of, character.name),
                    modifier = Modifier.fillMaxWidth())
                InfoRow(stringResource(R.string.species), character.species)
                InfoRow(stringResource(R.string.status), character.status)
                InfoRow(stringResource(R.string.origin), character.origin?.name)
                InfoRow(stringResource(R.string.type), character.type)
                InfoRow(stringResource(R.string.created), character.created)
            }
        }
        is Resource.Error -> {
            Text("Error Loading Character Details\n\n${result.message}")
        }
    }
}

@Composable
fun InfoRow(label: String, value: Instant?) {
    value?.let {
        val formatted = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault())
            .format(it)
        InfoRow(label, formatted)
    }
}

/** Displays a row conditionally, only if its non-empty. */
@Composable
fun InfoRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$label:")
            Text(text = value)
        }
    }
}
