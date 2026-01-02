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
    //Log.d("Navigation", "Detail: ${DestinasiDetail.routeWithArgs}")
   // Log.d("Navigation", "Edit: ${DestinasiEdit.routeWithArgs}")

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
    }
}