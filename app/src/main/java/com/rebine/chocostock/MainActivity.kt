package com.rebine.chocostock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rebine.chocostock.presentation.list.ChocolateListScreen
import com.rebine.chocostock.presentation.list.ChocolateListViewModel
import com.rebine.chocostock.presentation.list.ChocolateListViewModelFactory
import com.rebine.chocostock.ui.theme.ChocoStockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as ChocoStockApplication).repository

        setContent {
            ChocoStockTheme {
                val viewModel: ChocolateListViewModel = viewModel(
                    factory = ChocolateListViewModelFactory(repository)
                )
                ChocolateListScreen(viewModel = viewModel)
            }
        }
    }
}