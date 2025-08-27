package com.example.testekotlin

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.testekotlin.car.Car
import com.example.testekotlin.car.CarList.list
import com.example.testekotlin.sampledata.PreviewP

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CarComposable(@PreviewParameter(PreviewP::class)id:Int){
    lateinit var car:Car
    for(cars in list){
        if(cars.id == id){
            car = cars
            break
        }
    }
    val textFieldValue = remember{mutableStateOf(TextFieldValue("Km percorrido"))}
    var resultado by remember { mutableFloatStateOf(0.0f) }
    var showResult by remember{mutableStateOf(false)}
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(text = "Carro "+ (car.id).toString(), fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) {
            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal =  16.dp, vertical = 30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged{
                            state -> if (state.hasFocus) {
                        textFieldValue.value = textFieldValue.value.copy(
                            selection = TextRange(
                                0,
                                textFieldValue.value.text.length
                            )
                        )
                    }
                    },
                value = textFieldValue.value,
                onValueChange = {
                    textFieldValue.value = it
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    autoCorrectEnabled = true
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                ),
            )
            OutlinedTextField(
                value = car.bateria,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                ),
            )
            OutlinedButton(
                onClick ={
                   val calculateResult = calcularEconomia(km = textFieldValue.value.toString(), carga = car.bateria)
                    calculateResult?.let{
                        resultado = calculateResult
                        showResult = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                content = {
                    Text(text = "Calcular")
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
           if(showResult){
               ShowResultDialog(resultado, onDismissRequest = {
                   showResult = false
               })
           }
        }
    }
}

private fun calcularEconomia(km: String, carga: String): Float? {
    try {
        // Use a regex to get only numbers, including decimals if needed
        val kmCleaned = km.replace("[^\\d.]".toRegex(), "")

        // Return null if the cleaned string is empty or not a valid number
        if (kmCleaned.isEmpty()) {
            return null
        }

        val kmN = kmCleaned.toFloat()

        val regex = "\\d+".toRegex()
        val matchResult = regex.find(carga)

        if (matchResult != null) {
            val cargaN = matchResult.value.toFloat()
            if (cargaN != 0.0f) {
                return kmN / cargaN
            }
        }
    } catch (e: Exception) {
        Log.e("CalcularEconomia", "Error converting string to number", e)
    }
    return null
}

@SuppressLint("DefaultLocale")
@Composable
private fun ShowResultDialog(result: Float, onDismissRequest: () -> Unit){

    val numero = String.format("%.1f",result)

    Dialog(onDismissRequest = onDismissRequest){
        Card(
            modifier= Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Text(
                text = "Economia de $numero",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center)
        }
    }
}
