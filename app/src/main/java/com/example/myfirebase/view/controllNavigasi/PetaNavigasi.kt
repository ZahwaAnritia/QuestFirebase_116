package com.example.myfirebase.view.controllNavigasi

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirebase.view.DetailSiswaScreen
import com.example.myfirebase.view.EditSiswaScreen
import com.example.myfirebase.view.EntrySiswaScreen
import com.example.myfirebase.view.HomeScreen
import com.example.myfirebase.view.route.DestinasiDetail
import com.example.myfirebase.view.route.DestinasiEdit
import com.example.myfirebase.view.route.DestinasiEntry
import com.example.myfirebase.view.route.DestinasiHome

@Composable
fun DataSiswaApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // Log semua route yang terdaftar
    Log.d("Navigation", "=== REGISTERED ROUTES ===")
    Log.d("Navigation", "Home: ${DestinasiHome.route}")
    Log.d("Navigation", "Entry: ${DestinasiEntry.route}")
    Log.d("Navigation", "Detail: ${DestinasiDetail.routeWithArgs}")
    Log.d("Navigation", "Edit: ${DestinasiEdit.routeWithArgs}")

    HostNavigasi(navController = navController)
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {
        // Home Screen
        composable(DestinasiHome.route) {
            Log.d("Navigation", "Home screen rendered")
            HomeScreen(
                navigateToItemEntry = {
                    Log.d("Navigation", "Navigate to Entry")
                    navController.navigate(DestinasiEntry.route)
                },
                navigateToItemUpdate = { siswaId ->
                    val route = "${DestinasiDetail.route}/$siswaId"
                    Log.d("Navigation", "Navigate to Detail: $route")
                    navController.navigate(route)
                }
            )
        }

        // Entry Screen
        composable(DestinasiEntry.route) {
            Log.d("Navigation", "Entry screen rendered")
            EntrySiswaScreen(
                navigateBack = {
                    Log.d("Navigation", "Navigate back from Entry")
                    navController.popBackStack()
                }
            )
        }

        // Detail Screen
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetail.siswaIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            Log.d("Navigation", "Detail screen rendered")
            DetailSiswaScreen(
                navigateToEditItem = { itemId ->
                    val route = "${DestinasiEdit.route}/$itemId"
                    Log.d("Navigation", "Navigate to Edit: $route")
                    try {
                        navController.navigate(route)
                        Log.d("Navigation", "Navigation command sent successfully")
                    } catch (e: Exception) {
                        Log.e("Navigation", "Navigation failed: ${e.message}", e)
                    }
                },
                navigateBack = {
                    Log.d("Navigation", "Navigate back from Detail")
                    navController.popBackStack()
                }
            )
        }

        // Edit Screen
        composable(
            route = DestinasiEdit.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiEdit.itemIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            Log.d("Navigation", "=== EDIT SCREEN COMPOSABLE TRIGGERED ===")
            val siswaId = it.arguments?.getInt(DestinasiEdit.itemIdArg)
            Log.d("Navigation", "Edit screen siswaId from args: $siswaId")

            EditSiswaScreen(
                navigateBack = {
                    Log.d("Navigation", "Navigate back from Edit")
                    navController.popBackStack()
                },
                onNavigateUp = {
                    Log.d("Navigation", "Navigate up from Edit")
                    navController.navigateUp()
                }
            )
        }
    }
}