package com.example.testekotlin

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testekotlin.details.DetailsComposable
import com.example.testekotlin.home.HomeComposable
import com.example.testekotlin.insert.InsertComposable
import com.example.testekotlin.ui.TesteKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContent {
                TesteKotlinTheme {
                    NavigationHost()
                }
            }


    }

    @Composable
    fun NavigationHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainComposable(
                    onNavigateToHome = {
                        navController.navigate("home"){
                            popUpTo("main"){
                                inclusive = true
                            }
                        }

                    },
                )
            }
            
            composable("home") {
                HomeComposable(navToInsert = {
                    navController.navigate("insert")
                }, navToDetails = { id: Long ->
                    navController.navigate("details/${id}")
                })
            }

            composable("insert"){
                InsertComposable(
                    navToHome = {
                        navController.popBackStack()
                    }
                )
            }

            composable("details/{id}",
                arguments = listOf(
                    navArgument("id"){
                        type = NavType.LongType
                    }
                )){
                DetailsComposable(navToHome = {
                    navController.popBackStack()
                })
            }

        }
    }

    @Composable
    fun MainComposable(onNavigateToHome: () -> Unit) {
        Scaffold(
            containerColor = Color.White,
            modifier = Modifier.fillMaxSize()
        ){
            innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 25.dp, vertical = 40.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Image(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                    alignment = Alignment.TopCenter,
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = stringResource(R.string.icone_pessoa)
                )
                Text(
                    stringResource(R.string.mensagem_boas_vindas),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.md_theme_inversePrimary)
                )
                OutlinedButton(
                    onClick = {
                        onNavigateToHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorResource(R.color.md_theme_inversePrimary)
                    )
                ) {
                    Text(
                        stringResource(R.string.entrar),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun MainPreview() {
        TesteKotlinTheme { MainComposable(onNavigateToHome = {})
        }
        }

}