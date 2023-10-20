package com.karl.pokemonsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karl.pokemonsearch.ui.theme.PokemonSearchTheme
import com.karl.pokemonsearch.utils.Constants
import com.karl.pokemonsearch.utils.DataStorePreferencesHelper
import com.karl.pokemonsearch.utils.ScreenName
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonSearchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // block ui thread to read data from data store.
                    val isFirstLaunchFromDataStore = runBlocking {
                        DataStorePreferencesHelper.getInstance()
                            .getBooleanValue(
                                this@MainActivity,
                                Constants.IS_FIRST_LAUNCH_KEY,
                                true
                            )
                    }
                    val viewModel: SpeciesViewModel = viewModel()
                    NavGraph(
                        if (isFirstLaunchFromDataStore) ScreenName.SPLASH_SCREEN else ScreenName.HOME_SCREEN,
                        viewModel
                    )
                    // launch a coroutine to set first launch to false in first launch
                    LaunchedEffect(Unit) {
                        lifecycleScope.launch {
                            if (isFirstLaunchFromDataStore) {
                                DataStorePreferencesHelper.getInstance()
                                    .saveBooleanValue(
                                        this@MainActivity,
                                        Constants.IS_FIRST_LAUNCH_KEY,
                                        false
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}
