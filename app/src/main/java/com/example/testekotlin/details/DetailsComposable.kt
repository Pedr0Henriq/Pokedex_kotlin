package com.example.testekotlin.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsComposable(navToHome: () -> Unit, detailsModel : DetailsViewModel = hiltViewModel()){

    val deletePokemon = remember { mutableStateOf(false) }
    val pokemon = detailsModel.pokemon.collectAsState().value
    val painter = rememberAsyncImagePainter(model = pokemon?.photo)

    val openAlertDialog = remember { mutableStateOf(false) }

    when{
        openAlertDialog.value -> {
            ShowAlertDialog(
                onDismissRequest = {openAlertDialog.value = false},
                onConfirmation = {
                    openAlertDialog.value = false
                    detailsModel.deletePokemon()
                    navToHome()
                    },
                dialogTitle = "Remover Pokemon?",
                dialogText = "Deseja remover o ${pokemon?.name} ?",
                icon = Icons.Default.Info
            )
        }
    }



    pokemon?.let{
        val color = when(pokemon.types.first().type.name){
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

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = topAppBarColors(
                        containerColor = color,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navToHome()
                            },
                            modifier = Modifier.padding(end = 16.dp).size(24.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                openAlertDialog.value = true
                            },
                            modifier = Modifier.padding(end = 16.dp).size(24.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White
                            )
                        ){
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir"
                            )
                        }
                    },
                    title = {
                        Text(text = pokemon.name)
                    },
                )
            }
        ){
                innerPadding ->
            Card(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = color
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(400.dp)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painter,
                        contentDescription = "Foto do ${pokemon.name}",
                        modifier = Modifier.size(120.dp).fillMaxWidth()
                    )
                    Text(text ="Nome: ${pokemon.name}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text ="Peso: ${pokemon.weight}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text ="Altura: ${pokemon.height}",
                        fontWeight = FontWeight.Bold
                    )
                    var t = 1
                    for (type in pokemon.types){
                        Text(text ="Tipo $t: ${type.type.name}",
                            fontWeight = FontWeight.Bold
                        )
                        t++
                    }
                    t = 1
                    for (ability in pokemon.abilities){
                        Text(text ="Habilidade $t: ${ability.ability.name}",
                            fontWeight = FontWeight.Bold
                        )
                        t++
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
){
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Não")
            }
        }
    )
}