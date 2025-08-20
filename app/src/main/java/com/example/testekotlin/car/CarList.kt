package com.example.testekotlin.car

object CarList{

    val list = listOf<Car>(
        Car(
            id = 1,
            preco = "R$ 300.000,00",
            bateria = "300 kWh",
            potencia = "200cv",
            recarga = "30 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false,
        ),
        Car(
            id = 2,
            preco = "R$ 200.000,00",
            bateria = "200 kWh",
            potencia = "150cv",
            recarga = "40 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        ),
        Car(
            id = 3,
        preco = "R$ 100.000,00",
        bateria = "100 kWh",
        potencia = "80cv",
        recarga = "60 min",
        urlPhoto = "www.google.com.br",
        isFavorite = false
    ),
        Car(
            id = 4,
            preco = "R$ 500.000,00",
            bateria = "500 kWh",
            potencia = "350cv",
            recarga = "20 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        )
    )
}