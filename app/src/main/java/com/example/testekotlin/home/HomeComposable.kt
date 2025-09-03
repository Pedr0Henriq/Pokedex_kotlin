package com.example.testekotlin.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testekotlin.destination.Destination
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.testekotlin.R
import com.example.testekotlin.database.PokeDB


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(
    homeModel: HomeViewModel = hiltViewModel(),
    navToInsert: () -> Unit,
    navToDetails: (id:Long) -> Unit) {

    val tabNavController = rememberNavController()

    val pokemons = homeModel.pokemons.collectAsState()
    val favoritePokemons = homeModel.favoritePokemons.collectAsState()
    val destinations = remember { Destination.entries }
    val startDestination = Destination.ALL
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        homeModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(text = stringResource(R.string.inicio))
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) { }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ){
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            tabNavController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = {
                            Text(destination.label)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            IconButton(
                modifier = Modifier
                    .size(50.dp),
                onClick = {
                    navToInsert()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
            ) { Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Pokemon",
                modifier = Modifier
                    .size(30.dp),
            )}
        }
    ) {
            innerPadding -> NavHost(
        navController = tabNavController,
        startDestination = destinations.first().route,
        modifier = Modifier.padding(innerPadding)
    ){
        composable(Destination.ALL.route) {
            AllPokemons(pokemons = pokemons.value,navToDetails)
        }
        composable(Destination.FAVORITE.route) {
            FavoritePokemons(pokemons = favoritePokemons.value,navToDetails)
        }
    }
    }
}

@Composable
fun AllPokemons(pokemons:List<PokeDB>, navToDetails: (id:Long) -> Unit, viewModel: HomeViewModel = hiltViewModel()){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemons) { pokemon ->
            PokeCard(
                pokemon = pokemon,
                navToDetails = navToDetails,
                onToggleFavorite = { pokemonToUpdate, isFavorite ->
                    viewModel.updateFavorite(pokemon = pokemonToUpdate, favorite = isFavorite)
                })
        }
    }
}

@Composable
fun FavoritePokemons(pokemons:List<PokeDB>, navToDetails: (id:Long) -> Unit,viewModel: HomeViewModel = hiltViewModel()){

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemons) { pokemon ->
            PokeCard(
                pokemon = pokemon,
                navToDetails = navToDetails,
                onToggleFavorite = { pokemonToUpdate, isFavorite ->
                    viewModel.updateFavorite(pokemon = pokemonToUpdate, favorite = isFavorite)
                })
        }
    }
}


@Composable
fun PokeCard(pokemon: PokeDB, navToDetails: (id:Long) -> Unit, onToggleFavorite: (PokeDB, Boolean) -> Unit){

    val painter = rememberAsyncImagePainter(model = pokemon.photo)
    ElevatedCard(
        onClick = {
            navToDetails(pokemon.id)
        },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = when(pokemon.types.first().type.name){
                "normal" -> Color(0xFFA8A878)
                "fire" -> Color(0xFFF08030)
                "water" -> Color(0xFF6890F0)
                "electric" -> Color(0xFFF8D030)
                "grass" -> Color(0xFF78C850)
                "ice" -> Color(0xFF98D8D8)
                "fighting" -> Color(0xFFC03028)
                "poison" -> Color(0xFFA040A0)
                "ground" -> Color(0xFFE0C068)
                "flying" -> Color(0xFFA890F0)
                "psychic" -> Color(0xFFF85888)
                "bug" -> Color(0xFFA8B820)
                "rock" -> Color(0xFFB8A038)
                "ghost" -> Color(0xFF705898)
                "dragon" -> Color(0xFF7038F8)
                "dark" -> Color(0xFF705848)
                "steel" -> Color(0xFFB8B8D0)
                "fairy" -> Color(0xFFEE99AC)
                else -> Color.Gray
            }
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painter,
                    modifier = Modifier.size(120.dp).fillMaxWidth(),
                    contentDescription = "Imagem de ${pokemon.name}"
                )
                Text(text =pokemon.name,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = {
                    onToggleFavorite(pokemon,!pokemon.isFavorite)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                content = {
                    val icon = if (pokemon.isFavorite) Icons.Filled.Star else Icons.Outlined.Star
                    Icon(
                        imageVector = icon,
                        tint = if(pokemon.isFavorite) colorResource(R.color.md_theme_secondaryFixedDim) else Color.Black,
                        contentDescription = "Favorito",
                    )
                },
                )
        }

    }

}
