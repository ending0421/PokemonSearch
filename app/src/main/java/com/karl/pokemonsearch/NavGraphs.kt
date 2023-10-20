package com.karl.pokemonsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karl.pokemonsearch.utils.NavigationArguments
import com.karl.pokemonsearch.utils.ScreenName
import kotlinx.coroutines.delay

@Composable
fun NavGraph(startDestination: String = "splash", viewModel: SpeciesViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ScreenName.SPLASH_SCREEN) {
            SplashScreen(navController)
        }
        composable(ScreenName.HOME_SCREEN) {
            SpeciesList(onSpecyClick = { specyId ->
                navController.navigate("${ScreenName.DETAIL_SCREEN}/$specyId")
            }, viewModel)
        }
        composable(route = "${ScreenName.DETAIL_SCREEN}/{${NavigationArguments.SPECY_ID}}") { navBackStackEntry ->
            SpecyDetails(
                navController = navController,
                specyId = navBackStackEntry.arguments!!.getString(NavigationArguments.SPECY_ID)
                    ?.toInt() ?: 1,
            )
        }
    }
}


@Composable
fun SplashScreen(navController: NavController) {
    val splashDuration = remember { mutableIntStateOf(3000) }
    LaunchedEffect(Unit) {
        delay(splashDuration.intValue.toLong())
        // Navigate to next screen after delay
        navController.navigate(ScreenName.HOME_SCREEN)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.splash_icon),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(420.dp)
        )
    }
}