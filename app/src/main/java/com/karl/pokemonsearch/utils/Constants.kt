package com.karl.pokemonsearch.utils

import androidx.compose.ui.graphics.Color

object Constants {
    const val IS_FIRST_LAUNCH_KEY = "IS_FIRST_LAUNCH_KEY";
}

object ScreenName {
    const val SPLASH_SCREEN = "splash";
    const val HOME_SCREEN = "home";
    const val DETAIL_SCREEN = "detail";
}

object NavigationArguments {
    const val SPECY_ID = "specyId"
}

val ColorMap = mapOf(
    "brown" to Color(0xFFA52A2A),
    "purple" to Color(0xFF800080),
    "blue" to Color.Blue,
    "red" to Color.Red,
    "yellow" to Color.Yellow,
    "gray" to Color.Gray,
    "green" to Color.Green,
    "white" to Color.White,
    "pink" to Color(0xFFFFC0CB),
)
