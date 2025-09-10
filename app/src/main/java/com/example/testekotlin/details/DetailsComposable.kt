package com.example.testekotlin.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsComposable(navToHome: () -> Unit, detailsModel : DetailsViewModel = hiltViewModel()){
    val pokemon = detailsModel.pokemon.collectAsState().value
    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        ShowAlertDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                detailsModel.deletePokemon()
                navToHome()
            },
            dialogTitle = "Remover Pokémon?",
            dialogText = "Tem certeza que deseja remover o ${pokemon?.name} da sua lista?",
            icon = Icons.Default.Delete
        )
    }

    pokemon?.let { pk ->
        val typeColor = when(pk.types.first().type.name){
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
            else -> Color(0xFFC4C4C4)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = typeColor,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { navToHome() },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { openAlertDialog.value = true },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ){
                            Icon(Icons.Default.Delete, contentDescription = "Excluir")
                        }
                    },
                    title = {
                        Text(text = pk.name.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, color = Color.White)
                    },
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(typeColor.copy(alpha = 0.5f), Color.White)
                        )
                    )
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = pk.photo),
                            contentDescription = "Foto do ${pk.name}",
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .padding(8.dp)
                        )
                        Text(
                            text = pk.name.replaceFirstChar { it.uppercase() },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = typeColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            DetailText("Peso", "${pk.weight/10} kg")
                            DetailText("Altura", "${pk.height} dm")
                            DetailText("Tipo(s)", pk.types.joinToString { it.type.name.replaceFirstChar { char -> char.uppercase() } })
                            DetailText("Habilidade(s)", pk.abilities.joinToString { it.ability.name.replaceFirstChar { char -> char.uppercase() } })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailText(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
){
    AlertDialog(
        icon = { Icon(icon, contentDescription = "Ícone de aviso") },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Não")
            }
        }
    )
}