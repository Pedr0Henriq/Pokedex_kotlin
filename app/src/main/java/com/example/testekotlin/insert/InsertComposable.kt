package com.example.testekotlin.insert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testekotlin.R
import com.example.testekotlin.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertComposable(navToHome: () -> Unit, homeModel: HomeViewModel = hiltViewModel()){
    val snackbarHostState = remember { SnackbarHostState() }
    val pokemonName = remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = stringResource(R.string.adicionar),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navToHome()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(24.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
            )
        },
    ) {

            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            OutlinedTextField(
                value = pokemonName.value,
                onValueChange = {
                    pokemonName.value = it
                },
                label = {Text("Nome do Pokémon")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("Ex: squirtle")},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedBorderColor = colorResource(R.color.pokemon_red),
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = colorResource(R.color.pokemon_red),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = colorResource(R.color.pokemon_red)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if(pokemonName.value.isNotBlank()){
                        isLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            homeModel.fetchAndSavePokemon(query = pokemonName.value.lowercase().trim())
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                homeModel.showSnackBar()
                                navToHome()
                            }
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar("O nome do Pokémon não pode estar vazio!")
                        }
                    }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Buscar e Adicionar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }            }
        }

    }
}