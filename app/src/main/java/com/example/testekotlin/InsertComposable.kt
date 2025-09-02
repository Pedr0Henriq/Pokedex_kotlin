package com.example.testekotlin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.lifecycle.viewModelScope
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testekotlin.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertComposable(navToHome: () -> Unit, homeModel: HomeViewModel = hiltViewModel()){
    val snackbarHostState = remember { SnackbarHostState() }
    val pokemonName = remember { mutableStateOf("") }
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
                    Text(text = stringResource(R.string.adicionar))
                },
                actions = {
                    IconButton(
                        onClick = {
                            navToHome()
                        },
                        modifier = Modifier
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
                }
            )
        },
    ) {

            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedTextField(
                value = pokemonName.value,
                onValueChange = {
                    pokemonName.value = it
                },
                label = {Text("Nome")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("Insira o nome do pokemon")}
            )
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        homeModel.fetchAndSavePokemon(query = pokemonName.value)
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("Pokemón adicionado com sucesso")
                            navToHome()
                        }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Buscar")
            }
        }

    }
}