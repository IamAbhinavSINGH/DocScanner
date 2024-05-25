package com.example.docscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.docscanner.core.ui.theme.DocScannerTheme
import com.example.docscanner.feature_createpdf.presentation.archivedPdf.ArchivedPdfScreen
import com.example.docscanner.feature_createpdf.presentation.createpdf.CreatePdfScreen
import com.example.docscanner.feature_createpdf.presentation.newupdatespdf.WhatsNewScreen
import com.example.docscanner.feature_createpdf.presentation.searchpdf.SearchScreen
import com.example.docscanner.feature_createpdf.presentation.util.Screen
import com.example.docscanner.feature_createpdf.presentation.viewpdf.PdfViewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DocScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.CreatePdfScreen.route
                    ){
                        composable(
                            route = Screen.CreatePdfScreen.route
                        ){
                            CreatePdfScreen(navController = navController, activity = this@MainActivity)
                        }
                        composable(
                            route = Screen.SearchPdfScreen.route
                        ){
                            SearchScreen(navController = navController)
                        }
                        composable(
                            route = Screen.WhatsNewScreen.route
                        ){
                            WhatsNewScreen(navController = navController)
                        }
                        composable(
                            route = Screen.PdfViewScreen.route +
                            "?_id={_id}",
                            arguments = listOf(
                                navArgument(
                                    name = "_id"
                                ){
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ){
                            PdfViewScreen(
                                navController = navController,
                                activity = this@MainActivity
                            )
                        }
                        composable(
                            route = Screen.BinPdfScreen.route
                        ){
                            ArchivedPdfScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}