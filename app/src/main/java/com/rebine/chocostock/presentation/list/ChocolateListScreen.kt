package com.rebine.chocostock.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rebine.chocostock.domain.model.Chocolate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChocolateListScreen(
    viewModel: ChocolateListViewModel,
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val chocolates by viewModel.chocolates.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon stock de chocolat") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un chocolat")
            }
        }
    ) { padding ->
        if (chocolates.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aucun chocolat en stock. Appuie sur + pour en ajouter un.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(chocolates, key = { it.id }) { chocolate ->
                    ChocolateRow(
                        chocolate = chocolate,
                        onEditClick = { onEditClick(chocolate.id) },
                        onDeleteClick = { viewModel.deleteChocolate(chocolate) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ChocolateRow(
    chocolate: Chocolate,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chocolate.coverImagePath?.let { path ->
            Image(
                painter = rememberAsyncImagePainter(path),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(chocolate.title, style = MaterialTheme.typography.titleMedium)
                if (chocolate.isAnalyzing) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                }
            }
            Text(
                text = "Date limite : ${chocolate.expiryDateIso ?: "inconnue"}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (chocolate.analysisFailed) {
                Text(
                    text = "Analyse automatique échouée, modifie les infos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Modifier")
        }
        IconButton(onClick = { showDeleteConfirm = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Retirer du stock")
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Retirer ce chocolat du stock ?") },
            text = { Text("Cette action est définitive.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick()
                    showDeleteConfirm = false
                }) { Text("Retirer") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Annuler") }
            }
        )
    }
}