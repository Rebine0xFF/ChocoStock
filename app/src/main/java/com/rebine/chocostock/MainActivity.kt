package com.rebine.chocostock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.rebine.chocostock.presentation.add.AddChocolateScreen
import com.rebine.chocostock.presentation.add.AddChocolateViewModel
import com.rebine.chocostock.presentation.add.AddChocolateViewModelFactory
import com.rebine.chocostock.presentation.list.ChocolateListScreen
import com.rebine.chocostock.presentation.list.ChocolateListViewModel
import com.rebine.chocostock.presentation.list.ChocolateListViewModelFactory
import com.rebine.chocostock.presentation.edit.EditChocolateScreen
import com.rebine.chocostock.presentation.edit.EditChocolateViewModel
import com.rebine.chocostock.presentation.edit.EditChocolateViewModelFactory
import com.rebine.chocostock.ui.theme.ChocoStockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as ChocoStockApplication).repository

        setContent {
            ChocoStockTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        val application = LocalContext.current.applicationContext as ChocoStockApplication
                        val viewModel: ChocolateListViewModel = viewModel(
                            factory = ChocolateListViewModelFactory(application.repository)
                        )
                        ChocolateListScreen(
                            viewModel = viewModel,
                            onAddClick = { navController.navigate("add") },
                            onEditClick = { id -> navController.navigate("edit/$id") }
                        )
                    }
                    composable("add") {
                        val application = LocalContext.current.applicationContext as ChocoStockApplication
                        val viewModel: AddChocolateViewModel = viewModel(
                            factory = AddChocolateViewModelFactory(application.repository, application.analysisCoordinator)
                        )
                        AddChocolateScreen(
                            viewModel = viewModel,
                            onSaved = { navController.popBackStack() }
                        )
                    }
                    composable("edit/{chocolateId}") { backStackEntry ->
                        val chocolateId = backStackEntry.arguments?.getString("chocolateId") ?: return@composable
                        val application = LocalContext.current.applicationContext as ChocoStockApplication
                        val viewModel: EditChocolateViewModel = viewModel(
                            factory = EditChocolateViewModelFactory(application.repository)
                        )
                        EditChocolateScreen(
                            chocolateId = chocolateId,
                            viewModel = viewModel,
                            onSaved = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}