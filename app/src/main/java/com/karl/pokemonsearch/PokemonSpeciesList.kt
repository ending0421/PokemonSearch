@file:OptIn(ExperimentalMaterial3Api::class)

package com.karl.pokemonsearch

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apollographql.apollo3.api.Optional
import com.karl.pokemonsearch.utils.ColorMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val LOADING_ITEM_PAGE_SIZE = 10

@Composable
fun SpeciesList(onSpecyClick: (specyId: Int) -> Unit, viewModel: SpeciesViewModel) {
    var offset: Int? by remember { mutableStateOf(0) }
    val pokemonUiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var job: Job? = null
    // observe offset change to load data
    LaunchedEffect(offset) {
        job?.cancel()  // cancel previous job
        job = coroutineScope.launch {
            if (pokemonUiState.searchQuery.isNotEmpty()) {
                viewModel.appendPokemonResultList(
                    extracted(
                        offset,
                        pokemonUiState.searchQuery
                    )
                )
            }
        }
    }
    Column {
        BasicTextField(
            maxLines = 1,
            value = pokemonUiState.searchQuery,
            onValueChange = {
                viewModel.updateSearchQuery(it)
                viewModel.clearPokemonResult()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    coroutineScope.launch {
                        viewModel.appendPokemonResultList(
                            extracted(
                                offset,
                                pokemonUiState.searchQuery
                            )
                        )
                    }
                }
            ),
            textStyle = TextStyle(
                color = Color.Red, // 设置输入的文字颜色
                fontSize = 16.sp, // 设置字体大小
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier
                .padding(16.dp)
                .height(32.dp)
                .border(1.dp, color = Color.Blue, shape = RoundedCornerShape(8.dp))
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
                .offset(5.dp, 0.dp)
        )
        LazyColumn {
            items(pokemonUiState.pokemonSpeciesList) { specy ->
                SpecyItem(specy = specy, onClick = onSpecyClick)
            }
            item {
                if (pokemonUiState.pokemonSpeciesList.isNotEmpty()) {
                    LoadingItem()
                }
                offset = pokemonUiState.pokemonSpeciesList.size / LOADING_ITEM_PAGE_SIZE + 1
//                viewModel.updateOffsetState(pokemonUiState.pokemonSpeciesList.size / LOADING_ITEM_PAGE_SIZE + 1)
            }
        }
    }
}

private suspend fun extracted(
    offset: Int?,
    searchQuery: String,
): List<GetPokemonQuery.Pokemon_v2_pokemonspecy> {
    val response = apolloClient.query(
        GetPokemonQuery(
            Optional.present(offset),
            Optional.present(LOADING_ITEM_PAGE_SIZE),
            Optional.present(searchQuery),
        )
    ).execute()
    return response.data?.pokemon_v2_pokemonspecies.orEmpty()
}

@Composable
private fun SpecyItem(
    specy: GetPokemonQuery.Pokemon_v2_pokemonspecy,
    onClick: (specyId: Int) -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick(specy.id) },
        colors = ListItemDefaults.colors(
            containerColor = ColorMap[specy.pokemon_v2_pokemoncolor?.name] ?: Color.LightGray
        ),
        headlineContent = {
            // pokemon  name
            Text(text = specy.name)
        },
        supportingContent = {
            // pokemon id
            Text(text = specy.id.toString())
        },
    )
}

@Composable
private fun LoadingItem() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CircularProgressIndicator()
    }
}
