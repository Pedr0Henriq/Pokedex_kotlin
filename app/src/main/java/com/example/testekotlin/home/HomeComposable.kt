package com.example.testekotlin.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.testekotlin.R
import com.example.testekotlin.database.PokeDB
import com.example.testekotlin.destination.Destination // Certifique-se de que este import está correto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(
    homeModel: HomeViewModel = hiltViewModel(),
    navToInsert: () -> Unit,
    navToDetails: (id: Long) -> Unit
) {
    val tabNavController = rememberNavController()

    val pokemons = homeModel.pokemons.collectAsState().value
    val favoritePokemons = homeModel.favoritePokemons.collectAsState().value
    val filteredPokemons = homeModel.filterPokemons.collectAsState().value
    val destinations = remember { Destination.entries }
    val startDestination = Destination.ALL
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    val snackbarHostState = remember { SnackbarHostState() }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        homeModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(R.color.pokemon_background), // Usando uma cor mais suave para o fundo
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Cor primária para a AppBar
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = stringResource(R.string.inicio),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White // Texto branco para contraste
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter), // Certifique-se de ter um ícone de filtro estilizado
                            tint = Color.White, // Ícone branco
                            contentDescription = "Filtrar Tipo",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary, // Cor primária para a BottomBar
                contentColor = Color.White
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            tabNavController.navigate(route = destination.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = {
                            Text(destination.label, fontSize = 12.sp)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.pokemon_red),
                            selectedTextColor = Color.White,
                            indicatorColor = MaterialTheme.colorScheme.secondary // Cor de destaque para o item selecionado
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navToInsert() },
                containerColor = colorResource(R.color.pokemon_red),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Pokemon",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = destinations.first().route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.ALL.route) {
                AllPokemons(pokemons = filteredPokemons, navToDetails = navToDetails)
            }
            composable(Destination.FAVORITE.route) {
                FavoritePokemons(pokemons = favoritePokemons, navToDetails = navToDetails)
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Filtrar por Tipo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        TypesDropdownMenu(
                            onDismissRequest = { showDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypesDropdownMenu(onDismissRequest: () -> Unit, homeModel: HomeViewModel = hiltViewModel()) {
    val options = listOf(
        "todos", "normal", "fire", "water", "electric", "grass", "ice", "fighting",
        "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon",
        "dark", "steel", "fairy"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("todos") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedOptionText.replaceFirstChar { it.uppercase() }, // Capitaliza a primeira letra
            onValueChange = {},
            label = { Text("Tipo de Pokémon") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.8f), // Ajusta a largura do menu
            containerColor = Color.White,
        ) {
            options.forEach { selectOption ->
                DropdownMenuItem(
                    text = { Text(selectOption.replaceFirstChar { it.uppercase() }) },
                    onClick = {
                        selectedOptionText = selectOption
                        expanded = false
                        homeModel.onSearchQueryChanged(selectOption)
                        onDismissRequest()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    colors = MenuDefaults.itemColors(
                        textColor = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun AllPokemons(pokemons: List<PokeDB>, navToDetails: (id: Long) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    if (pokemons.isEmpty()) {
        EmptyListMessage("Nenhum Pokémon encontrado. Adicione novos Pokémon para começar!")
    } else {
        PokemonGrid(pokemons = pokemons, navToDetails = navToDetails, viewModel = viewModel)
    }
}

@Composable
fun FavoritePokemons(pokemons: List<PokeDB>, navToDetails: (id: Long) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    if (pokemons.isEmpty()) {
        EmptyListMessage("Nenhum Pokémon favorito. Adicione alguns!")
    } else {
        PokemonGrid(pokemons = pokemons, navToDetails = navToDetails, viewModel = viewModel)
    }
}

@Composable
fun PokemonGrid(pokemons: List<PokeDB>, navToDetails: (id: Long) -> Unit, viewModel: HomeViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pokemons) { pokemon ->
            PokeCard(
                pokemon = pokemon,
                navToDetails = navToDetails,
                onToggleFavorite = { pokemonToUpdate, isFavorite ->
                    viewModel.updateFavorite(pokemon = pokemonToUpdate, favorite = isFavorite)
                }
            )
        }
    }
}

@Composable
fun EmptyListMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_person), // Crie um ícone de pokeball vazia se não tiver.
            contentDescription = "Lista vazia",
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}


@Composable
fun PokeCard(pokemon: PokeDB, navToDetails: (id: Long) -> Unit, onToggleFavorite: (PokeDB, Boolean) -> Unit) {
    val painter = rememberAsyncImagePainter(model = pokemon.photo)
    val cardColor = when (pokemon.types.firstOrNull()?.type?.name) { // Usar firstOrNull para segurança
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
        else -> Color(0xFFC4C4C4) // Uma cor padrão mais neutra
    }

    ElevatedCard(
        onClick = { navToDetails(pokemon.id) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp), // Cantos mais arredondados
        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.8f)) // Cor do card com transparência
    ) {
        Box(
            modifier = Modifier
                .height(180.dp) // Altura fixa para os cards
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painter,
                    modifier = Modifier
                        .size(100.dp) // Tamanho da imagem ligeiramente ajustado
                        .background(Color.White.copy(alpha = 0.3f), CircleShape) // Fundo circular para a imagem
                        .padding(4.dp),
                    contentDescription = "Imagem de ${pokemon.name}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() }, // Capitaliza a primeira letra
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White // Texto branco para contraste com a cor do card
                )
            }
            IconButton(
                onClick = { onToggleFavorite(pokemon, !pokemon.isFavorite) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                val icon = if (pokemon.isFavorite) Icons.Filled.Star else Icons.Outlined.Star
                Icon(
                    imageVector = icon,
                    tint = if (pokemon.isFavorite) Color(0xFFFFD700) else Color.White, // Amarelo vibrante para favorito
                    contentDescription = "Favorito",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}