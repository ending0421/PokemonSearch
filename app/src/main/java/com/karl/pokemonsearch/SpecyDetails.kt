package com.karl.pokemonsearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.karl.pokemonsearch.LaunchDetailsState.ApplicationError
import com.karl.pokemonsearch.LaunchDetailsState.Loading
import com.karl.pokemonsearch.LaunchDetailsState.ProtocolError
import com.karl.pokemonsearch.LaunchDetailsState.Success

private sealed interface LaunchDetailsState {
    object Loading : LaunchDetailsState
    data class ProtocolError(val exception: ApolloException) : LaunchDetailsState
    data class ApplicationError(val errors: List<Error>) : LaunchDetailsState
    data class Success(val data: SpecyDetailQuery.Data) : LaunchDetailsState
}

@Composable
fun SpecyDetails(
    navController: NavController,
    specyId: Int
) {
    var state by remember { mutableStateOf<LaunchDetailsState>(Loading) }
    LaunchedEffect(Unit) {
        state = try {
            val response =
                apolloClient.query(SpecyDetailQuery(Optional.present(specyId))).execute()
            if (response.hasErrors()) {
                ApplicationError(response.errors!!)
            } else {
                Success(response.data!!)
            }
        } catch (e: ApolloException) {
            ProtocolError(e)
        }
    }
    when (val s = state) {
        Loading -> Loading()
        is ProtocolError -> ErrorMessage("Oh no... A protocol error happened: ${s.exception.message}")
        is ApplicationError -> ErrorMessage(s.errors[0].message)
        is Success -> SpecyDetails(navController, specyId, s.data)
    }
}

@Composable
private fun SpecyDetails(
    navController: NavController,
    specyId: Int,
    data: SpecyDetailQuery.Data,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                // name
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = data.pokemon_v2_ability.firstOrNull()?.name
                        ?: "No Item found for Id $specyId"
                )
                //id
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    text = data.pokemon_v2_ability.firstOrNull()?.id.toString()
                )
                Button({
                    navController.popBackStack()
                }) {
                    Text(text = "back")
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}