package com.example.testekotlin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testekotlin.car.Car
import com.example.testekotlin.car.CarList.list
import com.example.testekotlin.destination.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(onNavigateToCar: (id: Int) -> Unit) {

    val navController = rememberNavController()
    val carListState = remember {mutableStateOf(list)}
    val destinations = remember { Destination.entries }
    val startDestination = Destination.ALL
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(text = "Tela Home")
                }
            )
        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ){
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
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
                        }
                    )
                }
            }
        }
    ) {
            innerPadding -> NavHost(
        navController = navController,
        startDestination = destinations.first().route,
        modifier = Modifier.padding(innerPadding)
    ){
        composable(Destination.ALL.route) {
            CarList(carListState.value, onNavigateToCar = onNavigateToCar, onFavoriteClick = {
                    car -> carListState.value = carListState.value.map{
                if(it.id == car.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            })
        }
        composable(Destination.FAVORITE.route) {
            val favoriteCars = carListState.value.filter{it.isFavorite}
            FavoritesCarList(favoriteCars, onNavigateToCar = onNavigateToCar)
        }
    }

    }

}

@Composable
fun CarList(cars: List<Car>, onFavoriteClick: (Car) -> Unit,onNavigateToCar: (id:Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(cars) {
                car -> CarCard(car,onFavoriteClick, onNavigateToCar = onNavigateToCar)
        }
    }
}

@Composable
fun FavoritesCarList(cars: List<Car>,onNavigateToCar: (id:Int) -> Unit){
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cars){
                car ->
            CarCard(car, onNavigateToCar = onNavigateToCar)
        }
    }
}

@Composable
fun CarCard(car: Car,onFavoriteClick: ((Car) -> Unit)? = null, onNavigateToCar: (id:Int) -> Unit){
    ElevatedCard(
        onClick = {
            onNavigateToCar(car.id)
        },
        modifier = Modifier
            .size(width = 340.dp, height = 150.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ){
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text ="ID: " + car.id.toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text ="Preço: " + car.preco,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text ="Bateria: " + car.bateria,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text ="Recarga: " + car.recarga,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text ="Potência: " + car.potencia,
                    fontWeight = FontWeight.Bold
                )

            }
            IconButton(
                onClick = {

                    onFavoriteClick.apply {
                        onFavoriteClick?.invoke(car)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorito",
                        tint = if (car.isFavorite) Color.Yellow else Color.Black,
                    )
                },


                )
        }

    }
}