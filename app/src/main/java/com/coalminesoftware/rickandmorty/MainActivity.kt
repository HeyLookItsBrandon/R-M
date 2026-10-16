package com.coalminesoftware.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coalminesoftware.rickandmorty.ui.theme.RickAndMortyTheme

data class ScreenContext(val navController: NavHostController)

@Composable fun mockScreenContext() = ScreenContext(rememberNavController())

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                ContentHost()
            }
        }
    }
}

@Composable
private fun ContentHost() {
    val navController = rememberNavController()
    val context = remember(navController) { ScreenContext(navController) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(navController = navController, startDestination = "search", modifier = Modifier.padding(innerPadding)) {
            composable("search") {
                context.SearchScreen()
            }
            composable("details/{characterId}", arguments = listOf( navArgument("characterId") { type = NavType.IntType } )) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId") ?: error("Missing characterId")
                DetailsScreen(characterId)
            }
        }
    }
}
