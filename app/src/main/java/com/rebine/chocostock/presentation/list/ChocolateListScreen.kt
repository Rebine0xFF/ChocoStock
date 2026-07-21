package com.rebine.chocostock.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rebine.chocostock.domain.ExpiryUrgency
import com.rebine.chocostock.domain.ExpiryUrgencyCalculator
import com.rebine.chocostock.domain.model.Chocolate
import com.rebine.chocostock.presentation.common.DateFormatUtils
import com.rebine.chocostock.ui.theme.*
import com.rebine.chocostock.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChocolateListScreen(
    viewModel: ChocolateListViewModel,
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val chocolates by viewModel.chocolates.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Stock de chocolat 🍫🫠") }) },
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
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
            ) {
                items(chocolates, key = { it.id }) { chocolate ->
                    ChocolateRow(
                        chocolate = chocolate,
                        onEditClick = { onEditClick(chocolate.id) },
                        onDeleteClick = { viewModel.deleteChocolate(chocolate) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
    val urgency = ExpiryUrgencyCalculator.compute(chocolate.expiryDateIso)

    val pillColor = when (urgency) {
        ExpiryUrgency.URGENT -> CherryRed
        ExpiryUrgency.SOON -> Gold
        ExpiryUrgency.FAR -> MossGreen
        ExpiryUrgency.UNKNOWN -> WarmInk
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chocolate.coverImagePath != null) {
                Image(
                    painter = rememberAsyncImagePainter(chocolate.coverImagePath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(14.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(chocolate.title, style = MaterialTheme.typography.titleMedium)
                    if (chocolate.isAnalyzing) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = pillColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = DateFormatUtils.formatForDisplay(chocolate.expiryDateIso),
                        style = MaterialTheme.typography.labelLarge,
                        color = pillColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = WarmInk)
                }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chocolate_eat),
                        contentDescription = "Consommé",
                        tint = WarmInk
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Retirer ce chocolat du stock ?") },
            text = { Text("Cette action est définitive.") },
            confirmButton = {
                TextButton(onClick = { onDeleteClick(); showDeleteConfirm = false }) { Text("Oui, miam !") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Pas encore") }
            }
        )
    }
}