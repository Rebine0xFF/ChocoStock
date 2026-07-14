package com.rebine.chocostock.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rebine.chocostock.domain.model.Chocolate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChocolateListScreen(viewModel: ChocolateListViewModel) {
    val chocolates by viewModel.chocolates.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon stock de chocolat") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addFakeChocolate() }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter (test)")
            }
        }
    ) { padding ->
        if (chocolates.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucun chocolat en stock. Appuie sur + pour tester.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(chocolates, key = { it.id }) { chocolate ->
                    ChocolateRow(chocolate)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ChocolateRow(chocolate: Chocolate) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(chocolate.title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Périme le : ${chocolate.expiryDateIso ?: "date inconnue"}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}